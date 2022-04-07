//package algorithm.scc;
//
//import graph.Graph;
//import graph.GraphAlgorithmInterface;
//import graph.Node;
//import graph.sharedData.BFSSharedData;
//
//public class SCCForwardTraversalRest implements GraphAlgorithmInterface
//{
//    Graph<BFSSharedData> graph;
//    final int partitionId;
//    boolean[] isInActive;
//
//    public SCCForwardTraversalRest(int partitionId, Graph<BFSSharedData> graph, boolean[] isInActive) {
//        this.partitionId = partitionId;
//        this.graph = graph;
//        this.isInActive = isInActive;
//    }
//
//    @Override
//    public void execute() {
//        int partitionSize = graph.getPartition(partitionId).getSize();
//        int offset = partitionId * partitionSize;
//        BFSSharedData partition = graph.getPartition(partitionId);
//
//        for (int i = 0; i < partitionSize; i++) {
//            int nodeId = offset + i;
//            int colorId = partition.getVertexValue(i);
//
//            if (!isInActive[nodeId]) {
//                Node node = graph.getNode(nodeId);
//
//                int neighborListSize = node.neighborListSize();
//
//                for (int j = 0; j < neighborListSize; j++) {
//                    int neighborId = node.getNeighbor(j);
//
//                    if (!isInActive[neighborId]) {
//                        int nodeIdPositionInPart = graph.getNodePositionInPart(neighborId);
//                        partition.update(nodeIdPositionInPart, colorId);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void reset() {
//
//    }
//}
