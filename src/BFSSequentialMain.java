import algorithm.bfsSequential.BFSSequentialDriver;
import graph.Graph;
import graph.GraphUtil;
import graph.sharedData.BFSSharedData;

public class BFSSequentialMain
{
    public static void main(String[] args) {
        String inputFile = args[0];

        Graph<BFSSharedData> graph = Graph.getInstance(0,true,false);
        System.out.println("[DEBUG] Graph Loading ...");
        GraphUtil.load(graph, inputFile);
        System.out.println("[DEBUG] Graph Complete");
        graph.loadFinalize(0, BFSSharedData.class, false);

        BFSSequentialDriver driver = new BFSSequentialDriver(graph);
        driver.reset();


        System.out.println("[DEBUG] BFS Start");
        driver.run(0);
        System.out.println("[DEBUG] BFS END");
        System.out.println("[DEBUG] BFS Write ...");
        driver.writeBFSValues("BFS_test.txt");
        System.out.println("[DEBUG] END");
        System.exit(1);
    }
}
