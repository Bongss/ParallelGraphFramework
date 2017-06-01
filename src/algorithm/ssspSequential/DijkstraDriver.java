package algorithm.ssspSequential;

import graph.Graph;
import graph.Node;
import graph.sharedData.SSSPSharedData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.PriorityQueue;

public class DijkstraDriver
{
    Graph<SSSPSharedData> graph;
    PriorityQueue<Integer> activeQueue;
    int [] dist;

    final int source;
    final int maxNodeId;

    public DijkstraDriver(Graph<SSSPSharedData> graph, int source) {
        this.graph = graph;
        activeQueue = new PriorityQueue<>(new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int) (dist[o1] - dist[o2]);
            }
        });

        this.source = source;
        this.maxNodeId = graph.getMaxNodeId();
        dist = new int[maxNodeId + 1];
    }

    public void run() {
        for (int i = 0; i <= maxNodeId; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        dist[source] = 0;
        activeQueue.add(source);

        while (activeQueue.size() != 0) {
            int v = activeQueue.poll();

            Node node = graph.getNode(v);

            if (node != null) {
                int neighborListSize = node.neighborListSize();

                for (int i = 0; i < neighborListSize; i++) {
                    int u = node.getNeighbor(i);
                    relax(v, u, node.getWeight(i));
                }
            }
        }
    }

    public void relax(int src, int dest, int weight) {
        if (dist[dest] > dist[src] + weight) {
            dist[dest] = dist[src] + weight;
            activeQueue.add(dest);
        }
    }

    public void printDist() {
        try (FileWriter fw = new FileWriter("Dijkstra.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            for (int i = 0; i < dist.length; i++) {
//                String distance = String.format("%.3f", dist[i]);
//                out.println(i + " " + distance);
                out.println(i + "," + dist[i]);
            }
        }
        catch (IOException e) {

        }

    }
}


