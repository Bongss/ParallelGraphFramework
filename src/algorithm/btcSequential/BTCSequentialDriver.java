package algorithm.btcSequential;

import graph.Graph;
import graph.Node;
import graph.sharedData.SSSPSharedData;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class BTCSequentialDriver {
    Graph<SSSPSharedData> graph;
    PriorityQueue<Integer> activeQueue;
    LinkedList<Integer> predList;
    Stack<Integer> stack;
    int[] BC;
    int[] BCPartValues;
    double[] dist;
    int[] spCounts;

    final int maxNodeId;

    public BTCSequentialDriver(Graph<SSSPSharedData> graph) {
        this.graph = graph;
        this.maxNodeId = graph.getMaxNodeId();
        dist = new double[maxNodeId + 1];
        BC = new int[maxNodeId + 1];
        BCPartValues = new int[maxNodeId + 1];

        for (int i = 0; i <= maxNodeId; i++) {
            dist[i] = -1;
        }
    }

    public void run(int sourceId) {
        dist[sourceId] = 0;
        spCounts[sourceId] = 1;
        activeQueue.add(sourceId);

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
            while (!predList.isEmpty()) {
                int v = predList.pop();
                BCPartValues[v] = BCPartValues[v] + ((spCounts[v] / spCounts[w]) * (1 + BCPartValues[w]));
            }
            if (w != sourceId) {
                BC[w] = BC[w] + BCPartValues[w];
            }
        }
    }

    public void relax(int srcId, int destId, int weight) {
        if (dist[destId] > dist[srcId] + weight) {
            dist[destId] = dist[srcId] + weight;
            activeQueue.add(destId);

            spCounts[destId] = spCounts[srcId];
            predList.clear();
            predList.add(srcId);
        } else if (dist[destId] == dist[srcId] + weight) {
            predList.add(srcId);
            spCounts[destId]++;
        }
    }
}

