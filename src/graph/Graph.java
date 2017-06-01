package graph;

import graph.sharedData.PageRankSharedData;
import graph.sharedData.BFSSharedData;
import graph.sharedData.PersonalPageRankSharedData;
import graph.sharedData.SSSPSharedData;
import graph.sharedData.WCCSharedData;

public class Graph<T>
{
    final static int defaultSize = 10;
    static Graph instance = null;
    T sharedDataObject = null;

    final int expOfTaskSize;
    final boolean isDirected;
    final boolean isWeighted;

    Node[] nodes;

    int numNodes;
    int numEdges;
    int maxNodeId;
    int numTasks;

    Graph(int expOfTaskSize, boolean isDirected, boolean isWeighted)
    {
        this.expOfTaskSize = expOfTaskSize;
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
        nodes = new Node[defaultSize];
    }

    public static Graph getInstance(int expOfTaskSize, boolean isDirected, boolean isWeighted)
    {
        if (instance == null) {
            instance = new Graph(expOfTaskSize, isDirected, isWeighted);
        }
        return instance;
    }

    boolean addEdge(int srcNodeId, int destNodeId)
    {
        if (srcNodeId == destNodeId) {
            return false;
        }

        checkAndCreateNodes(srcNodeId, destNodeId);

        Node srcNode = nodes[srcNodeId];
        Node destNode = nodes[destNodeId];

        boolean isAdded = srcNode.addNeighborId(destNodeId); // Do not allow duplication

        if (isAdded) {
            srcNode.incrementOutDegree();
            destNode.incrementInDegree();
            if (isDirected) {
                numEdges++;
            }
            else {
                destNode.addNeighborId(srcNodeId);
                srcNode.incrementInDegree();
                destNode.incrementOutDegree();
                numEdges++;
            }
        }

        return isAdded;
    }

    boolean addEdge(int srcNodeId, int destNodeId, int weight)
    {
        if (srcNodeId == destNodeId) {
            return false;
        }
        checkAndCreateNodes(srcNodeId, destNodeId);

        Node srcNode = nodes[srcNodeId];
        Node destNode = nodes[destNodeId];

        boolean isAdded = srcNode.addNeighborId(destNodeId, weight);

        if (isAdded) {
            srcNode.incrementOutDegree();
            destNode.incrementInDegree();
            if (isDirected) {
                numEdges++;
            }
            else {
                destNode.addNeighborId(srcNodeId, weight);
                srcNode.incrementInDegree();
                destNode.incrementOutDegree();
                numEdges++;
            }
        }
        return isAdded;
    }

    void checkAndCreateNodes(int srcNodeId, int destNodeId)
    {
        int biggerNodeId = Math.max(srcNodeId, destNodeId);
        if (biggerNodeId > maxNodeId) {
            setMaxNodeId(biggerNodeId);
        }
        ensureNodesCapacity(biggerNodeId + 1);
        ensureNodeEntry(srcNodeId);
        ensureNodeEntry(destNodeId);
    }

    void ensureNodesCapacity(int capacity)
    { // TODO : vertex ID may not start with 1 but 10,000,000
        if (capacity > nodes.length) {
            int newCapacity = Math.max(nodes.length << 1, capacity);
            Node[] tmp = new Node[newCapacity];
            System.arraycopy(nodes, 0, tmp, 0, nodes.length);
            nodes = tmp;
        }
    }

    void ensureNodeEntry(int entry)
    {
        Node node = nodes[entry];
        if (node == null) {
            node = new Node();
            nodes[entry] = node;
            numNodes++;
        }
    }

    public Node getNode(int nodeId)
    {
        return nodes[nodeId];
    }

    public int getNumNodes()
    {
        return numNodes;
    }

    void setMaxNodeId(int nodeId)
    {
        this.maxNodeId = nodeId;
    }

    public int getMaxNodeId()
    {
        return maxNodeId;
    }

    // The following part is related to SharedData
    // TODO : need sortFlag
    public void loadFinalize(int asyncThreshold, Class<T> sharedDataObjectClass)
    {
//        adjListSort(asyncThreshold);

        int nodeCapacity = maxNodeId + 1;
        int taskSize = 1 << expOfTaskSize;
        numTasks = (nodeCapacity + taskSize - 1) / taskSize;

        if (sharedDataObjectClass == BFSSharedData.class) {
            sharedDataObject = (T) new BFSSharedData(nodeCapacity, asyncThreshold);
        }
        else if (sharedDataObjectClass == PageRankSharedData.class) {
            sharedDataObject = (T) new PageRankSharedData(nodeCapacity, asyncThreshold);
        }
        else if (sharedDataObjectClass == PersonalPageRankSharedData.class) {
            sharedDataObject = (T) new PersonalPageRankSharedData(nodeCapacity, asyncThreshold);
        }
        else if (sharedDataObjectClass == SSSPSharedData.class) {
            sharedDataObject = (T) new SSSPSharedData(nodeCapacity, numTasks, asyncThreshold);
        }
        else if (sharedDataObjectClass == WCCSharedData.class) {
            sharedDataObject = (T) new WCCSharedData(nodeCapacity, numTasks, asyncThreshold);
        }
    }
    // adjList descending sort based on indegree

    void adjListSort(int asyncThreshold)
    {
        for (int i = 0; i <= maxNodeId; i++) {
            if (nodes[i] == null) {
                continue;
            }

            int neighborListSize = nodes[i].neighborListSize();

            for (int j = 0; j < neighborListSize - 1; j++) {
                int nodeId1 = nodes[i].getQuick(j);
                for (int z = j + 1; z < neighborListSize; z++) {
                    int nodeId2 = nodes[i].getQuick(z);

                    if (nodes[nodeId1].getInDegree() < nodes[nodeId2].getInDegree()) {
                        nodes[i].replace(j, nodeId2);
                        nodes[i].replace(z, nodeId1);
                        nodeId1 = nodeId2;
                    }

                }
            }

            int thresholdIndex = Integer.MAX_VALUE;

            if (asyncThreshold == -1) { // Async 100%
                thresholdIndex = 0;
            }

            for (int j = 0; j < neighborListSize; j++) {
                int neighborId = nodes[i].getNeighbor(j);
                Node neighborNode = nodes[neighborId];

                if (neighborNode.getInDegree() <= asyncThreshold) {
                    thresholdIndex = j;
                    break;
                }
            }
            nodes[i].setThresholdIndex(thresholdIndex);
        }
    }

    public int getExpOfTaskSize()
    {
        return expOfTaskSize;
    }

    public T getSharedDataObject()
    {
        return sharedDataObject;
    }

    public int getTaskId(int nodeId)
    {
        return nodeId >> expOfTaskSize;
    }

    public int getNumTasks()
    {
        return numTasks;
    }

    public boolean isDirected()
    {
        return isDirected;
    }

    public boolean isWeighted()
    {
        return isWeighted;
    }
}
