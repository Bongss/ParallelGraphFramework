import algorithm.bfsSequential.BFSSequentialDriver;
import graph.Graph;
import graph.GraphUtil;
import graph.sharedData.BFSSharedData;

public class BFSSequentialMain
{
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputFile = args[1];

        Graph<BFSSharedData> graph = Graph.getInstance(0,true,false);
        System.err.println("[DEBUG] Graph Loading ...");
        GraphUtil.load(graph, inputFile);
        System.err.println("[DEBUG] Graph Complete");
        graph.loadFinalize(0, BFSSharedData.class, false);

        BFSSequentialDriver driver = new BFSSequentialDriver(graph);
        driver.reset();


        System.err.println("[DEBUG] BFS Start");
        long start = System.currentTimeMillis();
        driver.run(0);
        long elapsedTime = System.currentTimeMillis() - start;
        System.err.println("[DEBUG] BFS END");
        System.err.println("[DEBUG] BFS Write ...");
        driver.writeBFSValues(outputFile);
        System.err.println("[DEBUG] END");

        System.out.println("[ELAPSED_TIME] : " + elapsedTime / 1000.0);
        System.exit(1);
    }
}
