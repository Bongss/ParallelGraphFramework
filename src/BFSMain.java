//import algorithm.bfs.BFSDriver;
//import graph.Graph;
//import graph.GraphUtil;
//import graph.sharedData.BFSSharedData;
//
//public class BFSMain {
//    public static void main(String[] args) {
//        String inputFile = args[0];
//        int numThreads = Integer.parseInt(args[1]);
//
//        int expOfPartitionSize = 16; // 1 << 16;
//        int asyncRangeSize = ((1 << 16) * 0);
//
//        long start = System.currentTimeMillis();
//
//        Graph<BFSSharedData> graph = Graph.getInstance(expOfPartitionSize,true,false);
//        GraphUtil.load(graph, inputFile);
//        graph.generatePartition(asyncRangeSize, BFSSharedData.class);
//        //GraphUtil.finalizeLoading(graph);
//
//        long loadingTime = System.currentTimeMillis() - start;
//        System.out.println("[DEBUG] Graph Loading : " + ((double) loadingTime / 1000.0));
//
//        BFSDriver driver = new BFSDriver(graph, numThreads);
//
//        long[] elapsedTime = new long[20];
//
//        for (int i = 0; i < 20; i++) {
//            driver.reset();
//            start = System.currentTimeMillis();
//            driver.run();
//            elapsedTime[i] = System.currentTimeMillis() - start;
//            if (i >= 10) {
//                System.out.println("[DEBUG] elapsed time for iteration" + (i-10) + " : " + (((double) elapsedTime[i]) / (1000.0)));
//            }
//        }
//
//        System.exit(1);
//    }
//}