package algorithm.wcc;

import graph.Graph;
import graph.GraphAlgorithmInterface;
import graph.Node;
import graph.sharedData.WCCSharedData;

public class WCCExecutor implements GraphAlgorithmInterface {
    final Graph<WCCSharedData> graph;
    final WCCSharedData sharedDataObject;
    final int beginRange;
    final int endRange;

    public WCCExecutor(int beginRange, int endRange, Graph<WCCSharedData> graph) {
        this.beginRange = beginRange;
        this.endRange = endRange;
        this.graph = graph;
        sharedDataObject = graph.getSharedDataObject();
    }

    @Override
    public void execute() {
        int epoch = WCCDriver.getCurrentEpoch();
        for (int i = beginRange; i < endRange; i++) {
            Node srcNode = graph.getNode(i);

            if (srcNode == null) {
                continue;
            }

            int thresholdIndex = srcNode.getThresholdIndex();

            int curCompId = sharedDataObject.getCurCompId(i);
            int nextCompId = sharedDataObject.getNextCompId(i);

            if (curCompId == nextCompId) {
                continue;
            }

            sharedDataObject.setCurComponentId(i, nextCompId);

            int neighborListSize = srcNode.neighborListSize();

            for (int j = 0; j < neighborListSize; j++) {
                int destId = srcNode.getNeighbor(j);
                int destTaskId = graph.getTaskId(destId);

                if (destId <= nextCompId) {
                    continue;
                }

                boolean isUpdate;
                if (j < thresholdIndex) {
                    isUpdate = sharedDataObject.atomicUpdate(destId, nextCompId);
                } else {
                    isUpdate = sharedDataObject.asyncUpdate(destId, nextCompId);
                }

                if (isUpdate) {
                    sharedDataObject.setUpdatedEpoch(destTaskId, epoch);
                }
            }
        }
    }

    @Override
    public void reset() {

    }
}