package algorithm.wcc;

import graph.Graph;
import graph.Node;
import graph.sharedData.WCCSharedData;
import task.*;
import thread.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

public class WCCDriver {
    static volatile int currentEpoch;
    final int numThreads;
    final int taskSize;
    final int nodeCapacity;
    final int numTasks;

    boolean isDone;

    Graph<WCCSharedData> graph;
    LinkedBlockingQueue<Task> taskQueue;
    TaskWaitingRunnable runnable;
    WCCSharedData sharedDataObject;
    CyclicBarrier barriers;

    Task[] workerTasks;
    Task[] barrierTasks;

    public WCCDriver(Graph<WCCSharedData> graph, int numThreads) {
        this.graph = graph;
        this.numThreads = numThreads;
        this.taskSize = 1 << graph.getExpOfTaskSize();
        sharedDataObject = graph.getSharedDataObject();
        nodeCapacity = graph.getMaxNodeId() + 1;
        numTasks = (nodeCapacity + taskSize - 1) / taskSize;
        isDone = false;
        currentEpoch = 1;
        init();
    }

    public void init() {
        workerTasks = new Task[numTasks];
        barrierTasks = new Task[numThreads];

        barriers = new CyclicBarrier(numThreads + 1);
        taskQueue = new LinkedBlockingQueue<>();
        runnable = new TaskWaitingRunnable(taskQueue);

        ThreadUtil.createAndStartThreads(numThreads, runnable);

        for (int i = 0; i < numTasks; i++) {
            int beginRange = i * taskSize;
            int endRange = beginRange + taskSize;

            if (endRange > nodeCapacity) {
                endRange = nodeCapacity;
            }

            workerTasks[i] = new Task(new WCCExecutor(beginRange, endRange, graph));
        }

        for (int i = 0; i < numThreads; i++) {
            barrierTasks[i] = new Task(new BarrierTask(barriers));
        }
    }

    public void run() throws BrokenBarrierException, InterruptedException {
        pushAllTasks(workerTasks);
        pushAllTasks(barrierTasks);
        barriers.await();
        while (!isDone) {
            currentEpoch++;
            isDone = pushSomeTasks(workerTasks);
            pushAllTasks(barrierTasks);
            barriers.await();
        }
    }

    public boolean pushSomeTasks(Task[] tasks) {
        int count = 0;
        for (int i = 0; i < numTasks; i++) {
            if (sharedDataObject.getUpdatedEpoch(i) >= (currentEpoch - 1)) {
                taskQueue.offer(tasks[i]);
                count++;
            }
        }

        if (count == 0) {
            return true;
        }
        return false;
    }

    public void pushAllTasks(Task[] tasks) {
        for (int i = 0; i < tasks.length; i++) {
            taskQueue.offer(tasks[i]);
        }
    }


    public int getLargestWCC() {
        WCCSharedData sharedDataObject = graph.getSharedDataObject();
        int[] compIdCount = new int[nodeCapacity];
        for (int i = 0; i < nodeCapacity; i++) {
            if (graph.getNode(i) == null) {
                continue;
            }

            int compId = sharedDataObject.getNextCompId(i);
            compIdCount[compId]++;
        }

        int max = 0;
        for (int i = 0; i < graph.getMaxNodeId() + 1; i++) {
            max = Math.max(max, compIdCount[i]);
        }

        return max;
    }

    public void writeComponentIds(String outputFile) {
        try (FileWriter fw = new FileWriter(outputFile, false); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            for (int j = 0; j < nodeCapacity; j++) {
                Node node = graph.getNode(j);
                if (node != null) {
                    out.println(sharedDataObject.getCurCompId(j));
                } else {
                    out.println(-1);
                }
            }
        } catch (IOException e) {

        }
    }

    public void reset() {
        sharedDataObject.reset();
        isDone = false;
        currentEpoch = 1;
    }

    public static int getCurrentEpoch() {
        return currentEpoch;
    }
}