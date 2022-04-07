package algorithm.pagerank.personalized;

import graph.Graph;
import graph.GraphAlgorithmInterface;
import graph.Node;
import graph.sharedData.PersonalPageRankSharedData;

public class PersonalPageRankInit implements GraphAlgorithmInterface {
    Graph<PersonalPageRankSharedData> graph;
    PersonalPageRankSharedData sharedDataObject;

    final int beginRange;
    final int endRange;
    final int numSeeds;
    final double dampingFactor;

    double initialValue;
    double randomTeleportValue;
    boolean isFirst;

    PersonalPageRankInit(int beginRange, int endRange, Graph<PersonalPageRankSharedData> graph, double dampingFactor, int numSeeds) {
        this.graph = graph;
        this.dampingFactor = dampingFactor;
        this.beginRange = beginRange;
        this.endRange = endRange;
        this.numSeeds = numSeeds;

        sharedDataObject = graph.getSharedDataObject();
        initialValue = getInitPageRankValue(numSeeds, 0); //initial PageRank Value
        randomTeleportValue = getInitPageRankValue(numSeeds, dampingFactor);
        isFirst = true;
    }

    @Override
    public void execute() {
        if (!isFirst) {
            initialValue = randomTeleportValue;
        } else {
            initNextTable();
        }

        for (int i = beginRange; i < endRange; i++) {
            Node node = graph.getNode(i);
            if (node == null) {
                continue;
            }

            if (sharedDataObject.isNodeSeed(i)) {
                sharedDataObject.setVertexValue(i, initialValue);
            } else {
                sharedDataObject.setVertexValue(i, 0);
            }
        }
        isFirst = false;
    }

    public void initNextTable() {
        for (int i = beginRange; i < endRange; i++) {
            Node node = graph.getNode(i);
            if (node == null) {
                continue;
            }

            if (sharedDataObject.isNodeSeed(i)) {
                sharedDataObject.setNextVertexValue(i, randomTeleportValue);
            } else {
                sharedDataObject.setNextVertexValue(i, 0);
            }
        }
    }

    public double getInitPageRankValue(int numNodes, double dampingFactor) {
        return (1 - dampingFactor) / (double) numNodes;
    }

    public void reset() {
        isFirst = true;
        initialValue = getInitPageRankValue(numSeeds, 0);
    }
}
