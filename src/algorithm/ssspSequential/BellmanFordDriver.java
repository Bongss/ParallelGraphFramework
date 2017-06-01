package algorithm.ssspSequential;

import graph.Graph;
import graph.Node;
import graph.sharedData.SSSPSharedData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BellmanFordDriver
{
    Graph<SSSPSharedData> graph;
    double[] dist;

    final int source;
    final int maxNodeId;

    public BellmanFordDriver(Graph<SSSPSharedData> graph, int source) {
        this.graph = graph;

        this.source = source;
        this.maxNodeId = graph.getMaxNodeId();
        dist = new double[maxNodeId + 1];
    }

    public void run() {
        for (int i = 0; i <= maxNodeId; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
        }
        dist[source] = 0;

        for (int i = 0; i <= maxNodeId - 1; i++) {
            for (int z = 0; z <= maxNodeId; z++) {
                Node node = graph.getNode(z);
                if (node != null) {
                    int neighborListSize = node.size();
                    for (int j = 0; j < neighborListSize; j++) {
                        int neighborId = node.getNeighbor(j);
                        relax(z, neighborId, node.getWeight(j));
                    }
                }
            }
        }
    }

    public void relax(int src, int dest, double weight) {
        if (dist[dest] > dist[src] + weight) {
            dist[dest] = dist[src] + weight;
        }
    }

    public void printDist() {
        try (FileWriter fw = new FileWriter("sssp.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            for (int i = 0; i < dist.length; i++) {
                String distance = String.format("%.2f", dist[i]);
                out.println(i + " " + distance);
            }
        }
        catch (IOException e) {

        }

    }
}


