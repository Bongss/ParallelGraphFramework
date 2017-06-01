package algorithm.wcc;

import graph.Graph;
import graph.GraphAlgorithmInterface;
import graph.Node;
import graph.sharedData.WCCSharedData;

public class WCCExecutor implements GraphAlgorithmInterface
{
    final Graph<WCCSharedData> graph;
    final WCCSharedData sharedDataObject;
    final int beginRange;
    final int endRange;
//    final int threshold;
    final int numCheck;
    static boolean isFront;

    public WCCExecutor(int beginRange, int endRange, Graph<WCCSharedData> graph, int numPart, int numCheck) {
        this.graph = graph;
        this.beginRange = beginRange;
        this.endRange = endRange;
        this.numCheck = numCheck;
        sharedDataObject = graph.getSharedDataObject();

        isFront = true;
//        threshold = numPart * partitionSize;
    }

    @Override
    public void execute() {
        int epoch = WCCDriver.getCurrentEpoch();
//        if (epoch == 1) System.out.println("[DEBUG] Threshold : " + threshold);

        for (int i = beginRange; i < endRange; i++) {
            Node srcNode = graph.getNode(i);

            if (srcNode == null) {
                continue;
            }
            int srcInDegree = srcNode.getInDegree();

            int curCompId = sharedDataObject.getCurCompId(i);
            int nextCompId = sharedDataObject.getNextCompId(srcInDegree, i);

            if (isFront) {
                if (nextCompId != 0) {
                    continue;
                }
//                if (nextCompId > threshold) continue;
            }
            else {
                if (nextCompId == 0) {
                    continue;
                }
//                if (nextCompId <= threshold) continue;
            }

            if (curCompId == nextCompId) {
                continue;
            }

            sharedDataObject.setCurComponentId(i, nextCompId);

            int neighborListSize = srcNode.neighborListSize();

            boolean updateFlag = false;
            int check = 0;
            for (int j = 0; j < neighborListSize; j++) {
                int destId = srcNode.getNeighbor(j);                                            // 3% (256)     4% (4096)   4%(65536)
                int destTaskId = graph.getTaskId(destId); // bit remain operation

                if (destId <= nextCompId) {
                    continue;
                }

//                WCCDriver.incBefore();
                int destInDegree = graph.getNode(destId).getInDegree();
                if (sharedDataObject.update(destInDegree, destId, nextCompId)) { //destPartition.update(destPosition, nextCompId)) {
                    updateFlag = true;
                    sharedDataObject.setUpdatedEpoch(destTaskId, epoch);
//                    WCCDriver.incAfter();
//                    destPartition.setUpdatedEpoch(epoch);
                }
                else {
                    if (numCheck != -1 && !updateFlag) {
                        check++;
                        if (check >= numCheck) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void reset() {

    }

    public static void setIsFront(boolean value) {
        isFront = value;
    }
}