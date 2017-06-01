//package algorithm.scc;
//
//import graph.Graph;
//import graph.GraphAlgorithmInterface;
//import graph.sharedData.BFSSharedData;
//
//public class SCCForwardTraversalStart implements GraphAlgorithmInterface {
//    Graph<BFSSharedData> graph;
//    final int partitionId;
//
//    public SCCForwardTraversalStart(int partitionId, Graph<BFSSharedData> graph) {
//        this.partitionId = partitionId;
//        this.graph = graph;
//    }
//
//    @Override
//    public void execute() {
//        int partitionSize = graph.getPartition(partitionId).getSize();
//        int offset = partitionId * partitionSize;
//        BFSSharedData partition = graph.getPartition(partitionId);       // sharedData 안에 table이 있는게 의미상 이상함
//
//        for (int i = 0; i < partitionSize; i++) {
//            int nodeId = offset + i;
//            partition.setVertexValue(i, nodeId);
//        }
//    }
//
//    @Override
//    public void reset() {
//
//    }
//}
