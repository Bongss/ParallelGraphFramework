package algorithm.pagerank.original;

import graph.Graph;
import graph.GraphAlgorithmInterface;
import graph.Node;
import graph.sharedData.PageRankSharedData;

public class PageRankInit implements GraphAlgorithmInterface
{
    Graph<PageRankSharedData> graph;
    PageRankSharedData sharedDataObject;

    int beginRange;
    int endRange;
    double dampingFactor;
    double initialValue;
    double nextValue;
    boolean isFirst;

    PageRankInit(int beginRange, int endRange, Graph<PageRankSharedData> graph, double dampingFactor)
    {
        this.graph = graph;
        this.dampingFactor = dampingFactor;
        this.beginRange = beginRange;
        this.endRange = endRange;

        sharedDataObject = graph.getSharedDataObject();
        initialValue = getInitPageRankValue(0); //initial PageRank Value
        nextValue = getInitPageRankValue(dampingFactor);
        isFirst = true;
    }

    @Override
    public void execute()
    {
        if (!isFirst) {
            initialValue = nextValue;
        }
        else {
            initNextTable();
        }

        for (int i = beginRange; i < endRange; i++) {
            Node node = graph.getNode(i);

            if (node == null) {
                continue;
            }

            sharedDataObject.setVertexValue(i, initialValue);
        }
        isFirst = false;
    }

    public void initNextTable()
    {
        for (int i = beginRange; i < endRange; i++) {
            Node node = graph.getNode(i);
            if (node == null) {
                continue;
            }
            sharedDataObject.setNextVertexValue(i, nextValue);
        }
    }

    public double getInitPageRankValue(double dampingFactor)
    {
        return (1 - dampingFactor) / (double) graph.getNumNodes();
    }

    public void reset()
    {
        isFirst = true;
        initialValue = getInitPageRankValue(0);
    }
}
