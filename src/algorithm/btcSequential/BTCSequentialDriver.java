package algorithm.btcSequential;

import gnu.trove.list.array.TIntArrayList;
import graph.Graph;
import graph.Node;
import graph.sharedData.BTCSharedData;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

public class BTCSequentialDriver {
    Graph<BTCSharedData> graph;
    PriorityQueue<Integer> activeQueue;
    TIntArrayList[] predList;
    Stack<Integer> stack;
    double[] dist;
    int[] spCounts;
    double[] BCValues;                               // Betweenness and Centrality Value
    double[] BCPartValues;                           // Dependency Value
    final int maxNodeId;

    public BTCSequentialDriver(Graph<BTCSharedData> graph) {
        this.graph = graph;
        this.maxNodeId = graph.getMaxNodeId();
        dist = new double[maxNodeId + 1];
        spCounts = new int[maxNodeId + 1];
        BCValues = new double[maxNodeId + 1];
        BCPartValues = new double[maxNodeId + 1];

        activeQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int) (dist[o1] - dist[o2]);
            }
        });

        predList = new TIntArrayList[maxNodeId + 1];
        stack = new Stack<>();

        for (int i = 0; i <= maxNodeId; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            predList[i] = new TIntArrayList();
        }
    }

    public void run(int startId) {
        dist[startId] = 0;
        spCounts[startId] = 1;
        activeQueue.add(startId);

        while (activeQueue.size() != 0) {
            int srcId = activeQueue.poll();     // visited
            stack.push(srcId);

            Node node = graph.getNode(srcId);

            int neighborListSize = node.neighborListSize();

            for (int i = 0; i < neighborListSize; i++) {
                int destId = node.getNeighbor(i);
                relax(srcId, destId, node.getWeight(destId));
            }
        }

        while (!stack.empty()) {
            int w = stack.pop();
            while (predList[w].size() != 0) {
                int v = predList[w].removeAt(0);
                BCPartValues[v] = BCPartValues[v] + ((spCounts[v] / (double) spCounts[w]) * (1 + BCPartValues[w]));
            }
            if (w != startId) {
                BCValues[w] = BCValues[w] + BCPartValues[w];
            }
        }
    }

    public void relax(int srcId, int destId, int weight) {
        if (dist[destId] > dist[srcId] + weight) {
            dist[destId] = dist[srcId] + weight;
            if (!activeQueue.contains(destId)) {
                activeQueue.add(destId);
            }

            spCounts[destId] = spCounts[srcId];
            predList[destId].clear();
            predList[destId].add(srcId);
        } else if (dist[destId] == dist[srcId] + weight) {
            predList[destId].add(srcId);
            spCounts[destId] = spCounts[srcId] + spCounts[destId];
        }
    }

    public double[] getDistances() {
        return dist;
    }

    public int[] getSpCounts() {
        return spCounts;
    }

    public double[] getBCValues() {
        return BCValues;
    }
}

