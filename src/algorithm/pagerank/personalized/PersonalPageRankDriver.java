package algorithm.pagerank.personalized;

import graph.Graph;
import graph.Node;
import graph.sharedData.PersonalPageRankSharedData;
import task.BarrierTask;
import task.Task;
import thread.TaskWaitingRunnable;
import thread.ThreadUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.DoubleBinaryOperator;

/**
 * PageRank Algorithm Implementation
 **/
public class PersonalPageRankDriver {
    final int numThreads;
    final int iteration;
    final int taskSize;
    final double dampingFactor;
    final int numSeeds;
    final int nodeCapacity;
    final int numTasks;

    int[] seedSet;

    Graph<PersonalPageRankSharedData> graph;
    PersonalPageRankSharedData sharedDataObject;
    DoubleBinaryOperator updateFunction;
    LinkedBlockingQueue<Task> taskQueue;
    TaskWaitingRunnable runnable;
    CyclicBarrier barriers;
    CyclicBarrier exitBarriers;

    Task[] initTasks;
    Task[] workTasks;
    Task[] barrierTasks;
    Task[] exitBarrierTasks;

    public PersonalPageRankDriver(Graph<PersonalPageRankSharedData> graph, double dampingFactor, int iteration, int numThreads, String seedFile, int numSeeds) {
        this.graph = graph;
        this.dampingFactor = dampingFactor;
        this.numThreads = numThreads;
        this.iteration = iteration;
        this.taskSize = 1 << graph.getExpOfTaskSize();
        this.numSeeds = numSeeds;
        sharedDataObject = graph.getSharedDataObject();
        nodeCapacity = graph.getMaxNodeId() + 1;
        numTasks = (nodeCapacity + taskSize - 1) / taskSize;
        seedFileRead(seedFile);
        init();
    }

    public void init() {
        updateFunction = (prev, value) -> prev + value;
        PersonalPageRankSharedData.setUpdateFunction(updateFunction);

        initTasks = new Task[numTasks];
        workTasks = new Task[numTasks];
        barrierTasks = new Task[numThreads];
        exitBarrierTasks = new Task[numThreads];

        barriers = new CyclicBarrier(numThreads);
        exitBarriers = new CyclicBarrier(numThreads + 1);

        taskQueue = new LinkedBlockingQueue<>();
        runnable = new TaskWaitingRunnable(taskQueue);

        ThreadUtil.createAndStartThreads(numThreads, runnable);

        for (int i = 0; i < numTasks; i++) {
            int beginRange = i * taskSize;
            int endRange = beginRange + taskSize;

            if (endRange > nodeCapacity) {
                endRange = nodeCapacity;
            }

            initTasks[i] = new Task(new PersonalPageRankInit(beginRange, endRange, graph, dampingFactor, numSeeds));
            workTasks[i] = new Task(new PersonalPageRankExecutor(beginRange, endRange, graph, dampingFactor));
        }

        for (int i = 0; i < numThreads; i++) {
            barrierTasks[i] = new Task(new BarrierTask(barriers));
            exitBarrierTasks[i] = new Task(new BarrierTask(exitBarriers));
        }
    }

    public void run() throws BrokenBarrierException, InterruptedException {
        boolean isFirst = true;
        for (int i = 0; i < iteration; i++) {
            pushTasks(initTasks);
            pushTasks(exitBarrierTasks);
            exitBarriers.await();
            if (!isFirst) {
                sharedDataObject.initializedCallback();
            }
            pushTasks(workTasks);
            pushTasks(barrierTasks);
            isFirst = false;
        }
        pushTasks(exitBarrierTasks);
        exitBarriers.await();
    }

    public void pushTasks(Task[] tasks) {
        for (int i = 0; i < tasks.length; i++) {
            taskQueue.add(tasks[i]);
        }
    }

    //For JIT
    public void reset() {
        for (int i = 0; i < initTasks.length; i++) {
            initTasks[i].reset();
        }
        sharedDataObject.reset();

        for (int i = 0; i < numSeeds; i++) {
            int nodeId = seedSet[i];
            sharedDataObject.setSeedNode(nodeId);
        }
    }

    public void seedFileRead(String seedFile) {
        seedSet = new int[numSeeds];
        Path path = Paths.get(seedFile);
        int i = 0;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String seed;
            while ((seed = reader.readLine()) != null) {
                seedSet[i] = Integer.parseInt(seed);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Arrays.sort(seedSet);

    }

    public void writePageRanks(String outputFile) {
        try (FileWriter fw = new FileWriter(outputFile, false); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            sharedDataObject.initializedCallback();
            for (int j = 0; j < nodeCapacity; j++) {
                Node node = graph.getNode(j);
                if (node != null) {
                    out.println(sharedDataObject.getVertexValue(j));
                } else {
                    out.println(-1);
                }
            }
        } catch (IOException e) {

        }
    }

    public double _printPageRankSum() {
        ArrayList<Double> pageRankValues = new ArrayList<>();
        double sum = 0.0d;
        sharedDataObject.initializedCallback();

        for (int j = 0; j < nodeCapacity; j++) {
            Node node = graph.getNode(j);
            if (node == null) {
                continue;
            }
            int degree = node.getInDegree();
            pageRankValues.add(sharedDataObject.getVertexValue(j));
            sum += sharedDataObject.getVertexValue(j);
        }

        Collections.sort(pageRankValues, Collections.reverseOrder());

        //        for (int i = 0; i < 10; i++) {
//            System.out.println("[DEBUG] pageRank " + i + " : " + pageRankValues.get(i));
//        }
//        String pageRanksum = String.format("%.3f", sum);

        return sum;
    }

    public double[] _getPageRank(int[] sampleData) {
        double[] pageRank = new double[sampleData.length];

        for (int i = 0; i < sampleData.length; i++) {
            int nodeId = sampleData[i];
            pageRank[i] = sharedDataObject.getVertexValue(nodeId);

//            System.out.println(sampleData[i] + " : " + graph.getNode(node).getInDegree() + " : " + graph.getNode(node).getOutDegree());
        }
        return pageRank;
    }
}


