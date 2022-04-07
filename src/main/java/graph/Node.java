package graph;

import gnu.trove.list.array.TIntArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Node extends TIntArrayList {
    TIntArrayList weights = null;
    int thresholdIndex;
    int inDegree;
    int outDegree;

    Node() {
        weights = new TIntArrayList(10);
    }

    public boolean addNeighborId(int neighborNodeId) {
        int pos = binarySearch(neighborNodeId);
        if (pos >= 0) {
            return false;
        } else {
            pos = -(pos + 1);
            insert(pos, neighborNodeId);
            return true;
        }
    }

    public boolean addNeighborId(int neighborNodeId, int weight) {
        int pos = binarySearch(neighborNodeId);
        if (pos >= 0) {
            return false;
        } else {
            pos = -(pos + 1);
            insert(pos, neighborNodeId);
            weights.insert(pos, weight);
            return true;
        }
    }

    public int getNeighbor(int neighborNodeIdx) {
        return getQuick(neighborNodeIdx);
    }

    public int getWeight(int neighborNodeId) {
        int pos = binarySearch(neighborNodeId);
        return weights.get(pos);
    }

    public int neighborListSize() {
        return this._pos;
    }

    public void incrementInDegree() {
        inDegree++;
    }

    public void incrementOutDegree() {
        outDegree++;
    }

    public int getInDegree() {
        return inDegree;
    }

    public int getOutDegree() {
        return outDegree;
    }

    public void setThresholdIndex(int index) {
        thresholdIndex = index;
    }

    public int getThresholdIndex() {
        return thresholdIndex;
    }


    /*
    public void addReverseEdge(int neighborNodeId) {
        if (transposeNeighbors == null) {
            transposeNeighbors = new TIntArrayList();
        }

        int pos = transposeNeighbors.binarySearch(neighborNodeId);
        if (pos < 0) {
            pos = -(pos + 1);
            transposeNeighbors.insert(pos, neighborNodeId);
        }
    }
    */
}
