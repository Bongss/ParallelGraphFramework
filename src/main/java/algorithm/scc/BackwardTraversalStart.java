package algorithm.scc;

import graph.Graph;
import graph.GraphAlgorithmInterface;

public class BackwardTraversalStart implements GraphAlgorithmInterface{
    Graph graph;
    final int partitionId;
    boolean[] isInActive;

    public BackwardTraversalStart (int partitionId, Graph graph, boolean[] isInActive) {
        this.partitionId = partitionId;
        this.graph = graph;
        this.isInActive = isInActive;
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void reset() {

    }
}
