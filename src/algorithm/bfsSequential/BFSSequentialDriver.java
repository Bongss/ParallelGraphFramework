package algorithm.bfsSequential;

import graph.Graph;
import graph.Node;
import graph.sharedData.BFSSharedData;
import util.list.TIntLinkedListQueue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BFSSequentialDriver {
    int nodeCapacity;

    Graph<BFSSharedData> graph;
    BFSSharedData sharedDataObject;

    public BFSSequentialDriver(Graph<BFSSharedData> graph) {
        this.graph = graph;

        sharedDataObject = graph.getSharedDataObject();
        nodeCapacity = graph.getMaxNodeId() + 1;
    }

    public void run(int startNodeId) {
        int currentLevel = 1;
        sharedDataObject.setVertexValue(startNodeId, currentLevel);
        TIntLinkedListQueue activeQueue = new TIntLinkedListQueue();
        activeQueue.add(startNodeId);

        while (activeQueue.size() != 0) {
            int activeNodeId = activeQueue.poll();
            Node activeNode = graph.getNode(activeNodeId);
            currentLevel = sharedDataObject.getVertexValue(activeNodeId);

            int neighborListSize = activeNode.neighborListSize();

            for (int j = 0; j < neighborListSize; j++) {
                int destId = activeNode.getNeighbor(j);
                int destLevel = sharedDataObject.getVertexValue(destId);

                if (destLevel == 0) {
                    int levelForUpdate = currentLevel + 1;
                    sharedDataObject.setVertexValue(destId, levelForUpdate);
                    activeQueue.add(destId);
                }
            }
        }
        System.err.println("[DEBUG] LEVEL : " + currentLevel);
    }

    public void writeBFSValues(String outputFile) {
        try (FileWriter fw = new FileWriter(outputFile, false); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            for (int i = 0; i < nodeCapacity; i++) {
                Node node = graph.getNode(i);
                if (node != null) {
                    out.println(sharedDataObject.getVertexValue(i));
                } else {
                    out.println(-1);
                }
            }
        } catch (IOException e) {

        }
    }

    public void reset() {
        sharedDataObject.reset();
    }
}