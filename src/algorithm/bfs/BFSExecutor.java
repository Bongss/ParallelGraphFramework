package algorithm.bfs;

import graph.Graph;
import graph.GraphAlgorithmInterface;
import graph.Node;
import graph.sharedData.BFSSharedData;

public class BFSExecutor implements GraphAlgorithmInterface {

    Graph<BFSSharedData> graph;
    BFSSharedData sharedDataObject;

    final int beginRange;
    final int endRange;
    int threshold;

    BFSExecutor(int beginRange, int endRange, Graph<BFSSharedData> graph, int threshold) {
        this.graph = graph;
        this.beginRange = beginRange;
        this.endRange = endRange;
        sharedDataObject = graph.getSharedDataObject();
        this.threshold = threshold;
    }

    @Override
    public void execute() {
//        threshold = (threshold >> 1) + 5;
        final int currentLevel = sharedDataObject.getCurrentBFSLevel();
        for (int i = beginRange; i < endRange; i++) {
            if (sharedDataObject.getVertexValue(i) == currentLevel) {
                Node srcNode = graph.getNode(i);
                int neighborListSize = srcNode.neighborListSize();
                if (neighborListSize < threshold) {
                    continue;
                }

                for (int j = 0; j < neighborListSize; j++) {
                    int destId = srcNode.getNeighbor(j);
                    int destLevel = sharedDataObject.getVertexValue(destId);

                    if (destLevel == 0) {
                        int levelForUpdate = currentLevel + 1;
                        int destTaskId = graph.getTaskId(destId);
                        sharedDataObject.setVertexValue(destId, levelForUpdate);
                        sharedDataObject.setTaskLevels(destTaskId, levelForUpdate);
                    }
                }
            }
        }
    }

    @Override
    public void reset() {

    }
}