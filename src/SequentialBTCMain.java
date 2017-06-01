import algorithm.btcSequential.BTCSequentialDriver;
import graph.Graph;
import graph.GraphUtil;
import graph.sharedData.BTCSharedData;

public class SequentialBTCMain {
    public static void main(String[] args) {
        String inputFile = args[0];
        boolean isDirected = true;
        boolean isWeighted = true;

        Graph<BTCSharedData> graph = Graph.getInstance(0,isDirected, isWeighted);
        System.err.println("[DEBUG] Graph Loading ...");
        GraphUtil.load(graph, inputFile);
        System.err.println("[DEBUG] Graph Complete");

        BTCSequentialDriver driver = new BTCSequentialDriver(graph);

        System.err.println("[DEBUG] Betweenness and Centrality Start");
        driver.run(1);
        System.out.println("[DEBUG] Betweenness and Centrality  END");

        double[] distance = driver.getDistances();
        double[] BCValues = driver.getBCValues();
        int[] spCounts = driver.getSpCounts();

        for (int i = 0; i < distance.length; i++) {
            System.out.println(i + " Distance : " + distance[i] + "\tSSSP COUNT : " + spCounts[i] + "\tBCValues : " + BCValues[i]);
        }

        System.err.println("[DEBUG] END");
    }
}
