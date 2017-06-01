//package algorithm.scc;
//
//import graph.Graph;
//import graph.sharedData.BFSSharedData;
//import task.Task;
//import task.BarrierTask;
//import thread.TaskWaitingRunnable;
//import thread.ThreadUtil;
//
//import java.util.concurrent.CyclicBarrier;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//import java.util.function.IntBinaryOperator;
//
//public class SCCDriver {
//    int numThreads;
//
//    Graph<BFSSharedData> graph;
//    IntBinaryOperator updateFunction;
//    LinkedBlockingQueue<Task> taskQueue;
//    TaskWaitingRunnable runnable;
//    CyclicBarrier barriers;
//
//    Task[] trimTasks;
//    Task[] fwTraverseStartTasks;
//    Task[] fwTraverseStopTasks;
//    Task[] bwTraverseTasks;
//    Task[] barrierTasks;
//
//    boolean[] isInActiveNode;
//
//    public SCCDriver(Graph<BFSSharedData> graph, int numThreads) {
//        this.graph = graph;
//        this.numThreads = numThreads;
//        isInActiveNode = new boolean[graph.getMaxNodeId() + 1];
//
//        init();
//    }
//
//    public void init() {
//        int numPartitions = graph.getNumPartitions();
//        //graph.generateTransposeEdges(); // G^t = (V, E^T) TODO: Make generateTransposeEdges again
//
//        updateFunction = getUpdateFunction();
//        BFSSharedData.setUpdateFunction(updateFunction);
//
//        trimTasks = new Task[numPartitions];
//        fwTraverseStartTasks = new Task[numPartitions];
//        fwTraverseStopTasks = new Task[numPartitions];
//        bwTraverseTasks = new Task[numPartitions];
//        barrierTasks = new Task[numPartitions];
//
//        barriers = new CyclicBarrier(numThreads);
//        taskQueue = new LinkedBlockingQueue<>();
//        runnable = new TaskWaitingRunnable(taskQueue);
//
//        ThreadUtil.createAndStartThreads(numThreads, runnable);
//
//        for (int i = 0; i < numPartitions; i++) {
//            trimTasks[i] = new Task( new SCCTrim(i, graph, isInActiveNode));
//            fwTraverseStartTasks[i] = new Task(new SCCForwardTraversalStart(i, graph));
//            fwTraverseStopTasks[i] = new Task(new SCCForwardTraversalRest(i, graph, isInActiveNode));
//        }
//
//        for (int i = 0; i < numThreads; i++) {
//            barrierTasks[i] = new Task(new BarrierTask( barriers));
//        }
//    }
//
//    public void run() {
//        runOnce(trimTasks);
//        runOnce(fwTraverseStartTasks);
//        for (int i = 0; i < 5; i++) {
//            runOnce(fwTraverseStopTasks);
//            runOnce(barrierTasks);
//        }
//
//        busyWaitForSyncStopMilli(10);
//
//        for (int i = 0; i < isInActiveNode.length; i++) {
//            if (isInActiveNode[i] == true) {
//                System.out.print(" " + i);
//            }
//        }
//
//        System.out.println();
//        BFSSharedData partition = graph.getPartition(0);
//        for (int i = 0; i < partition.getSize(); i++) {
//            System.out.print(" " + partition.getVertexValue(i));
//        }
//    }
//
//    public void busyWaitForSyncStopMilli(int millisecond) {
//        while (taskQueue.size() != 0) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(millisecond);
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void runOnce(Task[] tasks) {
//        for (int i = 0; i < tasks.length; i++) {
//            taskQueue.add(tasks[i]);
//        }
//    }
//
//    public IntBinaryOperator getUpdateFunction() {
//        IntBinaryOperator updateFunction = (prev, value) -> {
//            int updateValue = prev;
//            if (prev < value) {
//                updateValue = value;
//            }
//            return updateValue;
//        };
//        return updateFunction;
//    }
//}
//
