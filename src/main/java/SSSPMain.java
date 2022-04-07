import algorithm.sssp.SSSPDriver;
import graph.Graph;
import graph.GraphUtil;
import graph.sharedData.SSSPSharedData;

import java.util.concurrent.BrokenBarrierException;

public class SSSPMain {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        final boolean isDirected = true;
        final boolean isWeighted = true;
        final boolean isInDegreeSorted = true;

        String inputFile = args[0];
        int numThreads = Integer.parseInt(args[1]);
        int delta = Integer.parseInt(args[2]);
        int sync = Integer.parseInt(args[3]);
        int numCheck = Integer.parseInt(args[4]);

        int expOfPartitionSize = 12; // 1 << 16;
        int asyncRangeSize = ((1 << 16) * sync); // 1 for async, 0 for sync

        long start = System.currentTimeMillis();

        Graph<SSSPSharedData> graph = Graph.getInstance(expOfPartitionSize, isDirected, isWeighted);
        System.out.println("[DEBUG] Graph Loading ... ");
        GraphUtil.load(graph, inputFile);
        graph.loadFinalize(asyncRangeSize, SSSPSharedData.class, isInDegreeSorted);

        System.out.println("[DEBUG] Loading Time : " + (System.currentTimeMillis() - start) / 1000.0);

        int srcId = 0;
//        Random random = new Random();
//        srcId = random.nextInt(4000000);
        SSSPDriver driver = new SSSPDriver(graph, numThreads, delta, srcId, numCheck);

        final int numRun = 20;
        long[] elapsedTime = new long[numRun];
        double timeSum = 0;

        System.out.println("[DEBUG] SSSP Running ... ");

        for (int i = 0; i < numRun; i++) {
            driver.reset();
            start = System.currentTimeMillis();
            driver.run();
            elapsedTime[i] = System.currentTimeMillis() - start;
            System.out.println("[DEBUG] elapsed time for iteration" + i + " : " + ((elapsedTime[i]) / (1000.0)));

            if (i >= 10) {
                timeSum += (elapsedTime[i] / 1000.0);
            }
        }
        System.out.println("[DEBUG] SSSP END");
        System.err.println("[DEBUG] File Write ...");
        driver.print();

        String averageTime = String.format("%.3f", (timeSum / 10));
        System.out.println(averageTime);

        System.exit(1);
    }
}