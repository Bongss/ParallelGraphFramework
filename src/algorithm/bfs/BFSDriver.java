//package algorithm.bfs;
//
//import graph.Graph;
//import graph.Node;
//import graph.sharedData.BFSSharedData;
//import task.*;
//import thread.*;
//import util.list.TIntLinkedListQueue;
//
//import java.util.concurrent.CyclicBarrier;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//import java.util.function.IntBinaryOperator;
//
//public class BFSDriver {
//    int numThreads;
//    boolean isDone;
//
//    Graph<BFSSharedData> graph;
//    IntBinaryOperator updateFunction;
//    LinkedBlockingQueue<Task> taskQueue;
//    TaskWaitingRunnable runnable;
//    CyclicBarrier barriers;
//
//    Task[] workTasks;
//    Task[] barrierTasks;
//
//    BFSExecutor[] bfsExecutors;
//
//    public BFSDriver(Graph<BFSSharedData> graph, int numThreads) {
//        this.graph = graph;
//        this.numThreads = numThreads;
//        this.isDone = false;
//        init();
//    }
//
//    public void init() {
//        updateFunction = getUpdateFunction();
//        BFSSharedData.setUpdateFunction(updateFunction);
//
//        workTasks = new Task[numPartitions];
//        bfsExecutors = new BFSExecutor[numPartitions];
//        barrierTasks = new Task[numThreads];
//        barriers = new CyclicBarrier(numThreads);
//
//        taskQueue = new LinkedBlockingQueue<>();
//        runnable = new TaskWaitingRunnable(taskQueue);
//
//        ThreadUtil.createAndStartThreads(numThreads, runnable);
//
//        for (int i = 0; i < numPartitions; i++) {
//            bfsExecutors[i] = new BFSExecutor(i, graph);
//            workTasks[i] = new Task(bfsExecutors[i]);
//        }
//
//        for (int i = 0; i < numThreads; i++) {
//            barrierTasks[i] = new Task(new BarrierTask(barriers));
//        }
//    }
//
//    public void run() {
//        After3level(); // Sequential Process 1,2,3
//        BFSExecutor.setLevel(4); // Parallel starting from level 4
//
//        while (true) {
//            runWorkerOnce(workTasks);
//            if (isDone) {
//                break;
//            }
//            runBarrierOnce(barrierTasks);
//            busyWaitForSyncStopMilli(10);
//            BFSExecutor.updateLevel();
//        }
//    }
//
//    public void runWorkerOnce(Task[] tasks) {
//        int count = 0;
//
//        for (int i = 0; i < tasks.length; i++) {
//            if (graph.getPartition(i).checkPartitionIsActive((byte) BFSExecutor.getLevel())) {
//                taskQueue.offer(tasks[i]);
//                count++;
//            }
//        }
//
//        if (count == 0) {
//            isDone = true;
//        }
//    }
//
//    public void runBarrierOnce(Task[] tasks) {
//        for (int i = 0; i < tasks.length; i++) {
//            taskQueue.offer(tasks[i]);
//        }
//    }
//
//    public void busyWaitForSyncStopMilli(int millisecond) {
//        while (taskQueue.size() != 0) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(millisecond);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public IntBinaryOperator getUpdateFunction() {
//        IntBinaryOperator updateFunction = (prev, value) -> value;
//        return updateFunction;
//    }
//
//    public void After3level() {
//        int startNodeId = 0;
//        BFSSharedData[] partitions = graph.getPartitions();
//        partitions[0].setVertexValue(0, 1);
//
//        TIntLinkedListQueue activeQueue = new TIntLinkedListQueue();
//        activeQueue.add(startNodeId);
//
//        for (int i = 0; i < 3; i++) {
//            int count = 0;
//            int curQueueSize = activeQueue.size();
//
//            int level = i + 1;
//            while (count < curQueueSize) {
//                int activeNodeId = activeQueue.poll();
//                Node node = graph.getNode(activeNodeId);
//                int neighborListSize = node.neighborListSize();
//
//                for (int j = 0; j < neighborListSize; j++) {
//                    int destId = node.getNeighbor(j);
//                    int destPartitionId = graph.getPartitionId(destId);
//                    int destPositionInPart = graph.getNodePositionInPart(destId);
//                    if (destPositionInPart >= partitions[destPartitionId].getSize()) {
//                        System.out.println("ERROR");
//                    }
//                    int destLevel = partitions[destPartitionId].getVertexValue(destPositionInPart);
//
//                    if (destLevel == 0) {
//                        int updateLevel = level + 1;
//                        partitions[destPartitionId].update(destPositionInPart, updateLevel);
//                        partitions[destPartitionId].setPartitionActiveValue((byte) updateLevel);
//                        activeQueue.add(destId);
//                    }
//                }
//                count++;
//            }
//        }
//    }
//
//    public void reset() {
//        for (int i = 0; i < graph.getNumPartitions(); i++) {
//            graph.getPartition(i).reset();
//        }
//        BFSExecutor.setLevel(1);
//        isDone = false;
//    }
//}