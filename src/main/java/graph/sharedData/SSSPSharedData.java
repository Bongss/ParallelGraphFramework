package graph.sharedData;

import atomic.AtomicIntegerArray;

public class SSSPSharedData {
    AtomicIntegerArray tables;
    AtomicIntegerArray bucketIds;
    volatile int[] innerIdx;
    int[] currMaxBucket;

    final int nodeCapacity;
    final int numTasks;
    final int asyncThreshold;

    public SSSPSharedData(int nodeCapacity, int numTasks, int asyncThreshold) {
        this.nodeCapacity = nodeCapacity;
        this.numTasks = numTasks;
        this.asyncThreshold = asyncThreshold;
    }

    public final void initializeTable() {
        tables = new AtomicIntegerArray(nodeCapacity);
        bucketIds = new AtomicIntegerArray(nodeCapacity);
        innerIdx = new int[numTasks];
        currMaxBucket = new int[numTasks];

        for (int i = 0; i < nodeCapacity; i++) {
            tables.set(i, Integer.MAX_VALUE);
            bucketIds.set(i, -1);
        }

        for (int i = 0; i < numTasks; i++) {
            innerIdx[i] = -1;
            currMaxBucket[i] = -1;
        }
    }

    public void setBucketId(int degree, int entry, int newId) {
        int prevId;
        if (degree < asyncThreshold) {
            prevId = bucketIds.get(entry);
            if (prevId == -1 || prevId > newId) {
                bucketIds.asyncSet(entry, newId);
            }
        } else {
            do {
                prevId = bucketIds.get(entry);
                if (prevId != -1 && newId >= prevId) {
                    break;
                }
            }
            while (!bucketIds.compareAndSet(entry, prevId, newId));
        }
    }

    public int getBucketId(int degree, int entry) {
        if (degree < asyncThreshold) {
            return bucketIds.asyncGet(entry);
        } else {
            return bucketIds.get(entry);
        }
    }

    public void setInnerIdx(int taskId, int value) {
        if (innerIdx[taskId] != value) {
            innerIdx[taskId] = value;
        }
    }

    public void setInnerIdxAll(int value) {
        for (int i = 0; i < numTasks; i++) {
            innerIdx[i] = value;
        }
    }

    public int getInnerIdx(int taskId) {
        return innerIdx[taskId];
    }

    public void setCurrMaxBucket(int taskId, int value) {
        if (currMaxBucket[taskId] < value) {
            currMaxBucket[taskId] = value;
        }
    }

    public int getCurrMaxBucket(int taskId) {
        return currMaxBucket[taskId];
    }

    public final int getVertexValue(int degree, int entry) {
        if (degree < asyncThreshold) {
            return tables.asyncGet(entry);
        } else {
            return tables.get(entry);
        }
    }

    public final boolean update(int degree, int entry, int newDist) {
        int currDist;
        if (degree < asyncThreshold) { // TODO : think about multiple ranges in a single sharedData
            currDist = tables.asyncGet(entry);
            if (newDist < currDist) {
                tables.asyncSet(entry, newDist);
                return true;
            }
            return false;
        } else {
            do {
                currDist = tables.get(entry);
                if (newDist >= currDist) {
                    return false;
                }
            }
            while (!tables.compareAndSet(entry, currDist, newDist));
            return true;
        }
    }


    public void reset() {
        initializeTable();
    }
}