//import algorithm.scc.SCCDriver;
//import graph.Graph;
//import graph.GraphUtil;
//import graph.sharedData.BFSSharedData;
//
//public class SCCMain {
//    public static void main(String[] args) {
//        String inputFile = args[0];
//        int numThreads = Integer.parseInt(args[1]);
//
//        int expOfPartitionSize = 4; // 2 ^ n     For PartitionSize
//        int numValuesPerNode = 1;
//        int asyncRangeSize = 0;//(int) ((1 << 16) * 0.3);
//
//        long start = System.currentTimeMillis();
//        Graph<BFSSharedData> graph = Graph.getInstance(expOfPartitionSize,true,false);
//        GraphUtil.load(graph, inputFile);
//        graph.generatePartition(asyncRangeSize, BFSSharedData.class);
//        long elapsedTime = System.currentTimeMillis() - start;
//        System.out.println("Graph Load Time : " + elapsedTime / (double) 1000 + " \n");
//
//        //GraphUtil.finalizeLoading(graph);
//
//        SCCDriver driver = new SCCDriver(graph, numThreads);
//        driver.run();
//    }
//}
