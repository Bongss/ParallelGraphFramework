package algorithm.bfs;

import graph.Graph;
import graph.Node;
import graph.sharedData.BFSSharedData;
import task.*;
import thread.*;
import util.list.TIntLinkedListQueue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

public class BFSDriver {
    int numThreads;
    int numTasks;
    int taskSize;
    int nodeCapacity;

    Graph<BFSSharedData> graph;
    BFSSharedData sharedDataObject;
    LinkedBlockingQueue<Task> taskQueue;
    TaskWaitingRunnable runnable;
    CyclicBarrier barriers;

    Task[] workTasks;
    Task[] barrierTasks;

    public BFSDriver(Graph<BFSSharedData> graph, int numThreads) {
        this.graph = graph;
        this.numThreads = numThreads;

        sharedDataObject = graph.getSharedDataObject();
        taskSize = 1 << graph.getExpOfTaskSize();
        nodeCapacity = graph.getMaxNodeId() + 1;
        numTasks = (nodeCapacity + taskSize - 1) / taskSize;

        init();
    }

    public void init() {
        workTasks = new Task[numTasks];
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
            workTasks[i] = new Task(new BFSExecutor(beginRange, endRange, graph));
        }

        for (int i = 0; i < numThreads; i++) {
            barrierTasks[i] = new Task(new BarrierTask(barriers));
        }
    }

    public void run() throws BrokenBarrierException, InterruptedException {
        boolean isTermination;
        After3level(0); // input : startNode Id, Sequential Process 1,2,3
        sharedDataObject.setCurrentBFSLevel(4);

        while (true) {
            isTermination = runWorkerOnce(workTasks);
            if (isTermination) {
                break;
            }
            runBarrierOnce(barrierTasks);
            barriers.await();
            sharedDataObject.incrementBFSLevel();
        }
    }

    public boolean runWorkerOnce(Task[] tasks) {
        int nonActiveTaskCount = 0;
        final int curBFSLevel = sharedDataObject.getCurrentBFSLevel();

        for (int i = 0; i < tasks.length; i++) {
            if (sharedDataObject.checkTaskLevels(i, curBFSLevel)) {
                taskQueue.offer(tasks[i]);
                nonActiveTaskCount++;
            }
        }

        if (nonActiveTaskCount == 0) {
            return true;
        }
        return false;
    }

    public void runBarrierOnce(Task[] tasks) {
        for (int i = 0; i < tasks.length; i++) {
            taskQueue.offer(tasks[i]);
        }
    }

    public void After3level(int startNodeId) {
        sharedDataObject.setVertexValue(startNodeId, 1);
        //need set the start
        TIntLinkedListQueue activeQueue = new TIntLinkedListQueue();
        activeQueue.add(startNodeId);

        for (int level = 1; level < 4; level++) {
            int levelBoundaryInQueue = 0;
            int curQueueSize = activeQueue.size();

            while (levelBoundaryInQueue < curQueueSize) {
                int activeNodeId = activeQueue.poll();
                Node activeNode = graph.getNode(activeNodeId);
                int neighborListSize = activeNode.neighborListSize();

                for (int j = 0; j < neighborListSize; j++) {
                    int destId = activeNode.getNeighbor(j);
                    int destLevel = sharedDataObject.getVertexValue(destId);

                    if (destLevel == 0) {
                        int destTaskId = graph.getTaskId(destId);
                        int levelForUpdate = level + 1;
                        sharedDataObject.setVertexValue(destId, levelForUpdate);
                        sharedDataObject.setTaskLevels(destTaskId, levelForUpdate);
                        activeQueue.add(destId);
                    }
                }
                levelBoundaryInQueue++;
            }
        }
    }

    public void writeBFSValues(String outputFile) {
        try (FileWriter fw = new FileWriter(outputFile, false); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            for (int i = 0; i < nodeCapacity; i++) {
                Node node = graph.getNode(i);
                if (node != null) {
                    out.println(sharedDataObject.getVertexValue(i));
                } else {
                    out.println(-1);
                }
            }
        } catch (IOException e) {

        }
    }

    public void reset() {
        sharedDataObject.reset();
    }
}