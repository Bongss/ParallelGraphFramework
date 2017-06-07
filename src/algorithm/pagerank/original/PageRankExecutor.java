package algorithm.pagerank.original;

import graph.Graph;
import graph.GraphAlgorithmInterface;
import graph.Node;
import graph.sharedData.PageRankSharedData;

public class PageRankExecutor implements GraphAlgorithmInterface {
    Graph<PageRankSharedData> graph;
    PageRankSharedData sharedDataObject;
    Node srcNode;

    int beginRange;
    int endRange;
    double dampingFactor;
    int maxInDegreeIndex;

    PageRankExecutor(int beginRange, int endRange, Graph<PageRankSharedData> graph, double dampingFactor, int maxInDegreeIndex) {
        this.graph = graph;
        this.dampingFactor = dampingFactor;
        this.beginRange = beginRange;
        this.endRange = endRange;
        this.maxInDegreeIndex = maxInDegreeIndex;
        sharedDataObject = graph.getSharedDataObject();
    }

    @Override
    public void execute() {
        for (int i = beginRange; i < endRange; i++) {
            srcNode = graph.getNode(i);

            if (srcNode == null) {
                continue;
            }

            final int thresholdIndex = srcNode.getThresholdIndex();
            int neighborListSize = srcNode.neighborListSize();
            double curPageRank = sharedDataObject.getVertexValue(i);

            if (curPageRank == 0) {
                continue;
            }

            double scatterPageRank = dampingFactor * (curPageRank / (double) neighborListSize);

            for (int j = 0; j < neighborListSize; j++) {
                int destId = srcNode.getNeighbor(j);

                if (j == maxInDegreeIndex) {
                    sharedDataObject.setHighDegreePageRank((int) Thread.currentThread().getId(), maxInDegreeIndex);
                }

                if (j < thresholdIndex) {
                    sharedDataObject.atomicUpdateNextTable(destId, scatterPageRank);
                } else {
                    sharedDataObject.asyncUpdateNextTable(destId, scatterPageRank);
                }
            }
        }
    }

    @Override
    public void reset() {

    }
}
