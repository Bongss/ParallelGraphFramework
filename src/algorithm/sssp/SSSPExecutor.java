package algorithm.sssp;

import gnu.trove.list.array.TIntArrayList;
import graph.Graph;
import graph.GraphAlgorithmInterface;
import graph.Node;
import graph.sharedData.SSSPSharedData;

public class SSSPExecutor implements GraphAlgorithmInterface
{

    Graph<SSSPSharedData> graph;
    SSSPSharedData sharedDataObject;

    final int beginRange;
    final int endRange;
    final int delta;
    final int numCheck;
    static volatile boolean isHeavy;

    TIntArrayList[] lightEdges;
    TIntArrayList[] lightWeights;
    TIntArrayList[] heavyEdges;
    TIntArrayList[] heavyWeights;
    TIntArrayList edges = null;
    TIntArrayList weights = null;

    SSSPExecutor(int beginRange, int endRange, Graph<SSSPSharedData> graph, int delta, int numCheck) {
        this.graph = graph;
        this.beginRange = beginRange;
        this.endRange = endRange;
        this.delta = delta;
        this.numCheck = numCheck;
        this.lightEdges = SSSPDriver.getLightEdges();
        this.heavyEdges = SSSPDriver.getHeavyEdges();
        this.lightWeights = SSSPDriver.getLightWeights();
        this.heavyWeights = SSSPDriver.getHeavyWeights();
    }

    @Override
    public void execute() {
        int bucketIdx = SSSPDriver.getBucketIdx();

        for (int i = beginRange; i < endRange; i++) {
            int srcNodeInDegree = graph.getNode(i).getInDegree();
            int currBucket = sharedDataObject.getBucketId(srcNodeInDegree, i);
            if (currBucket == bucketIdx) {
                Node srcNode = graph.getNode(i);

                if (srcNode == null) {
                    continue;
                }

                update(null, i, bucketIdx);
            }
        }
    }

    public void update(Node srcNode, int nodeId, int bucketIdx) {
        if (!isHeavy) {
            edges = lightEdges[nodeId];
            weights = lightWeights[nodeId];
        }
        else {
            edges = heavyEdges[nodeId];
            weights = heavyWeights[nodeId];
        }

        int neighborListSize = edges.size();
        int InnerIdx = SSSPDriver.getInnerIdx();

        int myDist = sharedDataObject.getVertexValue(graph.getNode(nodeId).getInDegree(), nodeId);
        boolean updateFlag = false;
        int check = 0;

        for (int i = 0; i < neighborListSize; i++) {
            int destId = edges.getQuick(i);
            int destTaskId = graph.getTaskId(destId);
            int destInDegree = graph.getNode(destId).getInDegree();
            int newDist = myDist + weights.getQuick(i);
            boolean updated = sharedDataObject.update(destInDegree,destId, newDist);

            //            SSSPDriver.incBefore();
            if (updated) {
//                SSSPDriver.incAfter();
                updateFlag = true;
                int newBucketId = newDist >> delta;
                sharedDataObject.setBucketId(destInDegree, destId, newBucketId);
                sharedDataObject.setCurrMaxBucket(destTaskId, newBucketId);
                if (newBucketId == bucketIdx) {
                    sharedDataObject.setInnerIdx(destTaskId, InnerIdx);
                }
            }
            else {
                if (numCheck != -1 && !updateFlag) {
                    check++;
                    if (check >= numCheck) {
//                        SSSPDriver.incKillAfterFive();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void reset() {

    }

    public static void setIsHeavy(boolean value) {
        isHeavy = value;
    }
}