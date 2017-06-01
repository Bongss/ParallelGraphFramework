package graph.sharedData;

import atomic.AtomicIntegerArray;

public class WCCSharedData
{
    AtomicIntegerArray nextCompIds;
    int[] curCompIds;
    volatile int[] updatedEpochs;

    final int nodeCapacity;
    final int numTasks;
    final int asyncThreshold;

    public WCCSharedData(int nodeCapacity, int numTasks, int asyncThreshold) {
        this.nodeCapacity = nodeCapacity;
        this.numTasks = numTasks;
        this.asyncThreshold = asyncThreshold;
    }

    public void initializeTable() {
        nextCompIds = new AtomicIntegerArray(nodeCapacity);
        curCompIds = new int[nodeCapacity];
        updatedEpochs = new int[numTasks];

        for (int i = 0; i < nodeCapacity; i++) {
            curCompIds[i] = -1;
            nextCompIds.set(i, i);
        }
    }

    public void setUpdatedEpoch(int taskId, int value) {
        if (updatedEpochs[taskId] != value) {
            updatedEpochs[taskId] = value;
        }
    }

    public int getUpdatedEpoch(int taskId) {
        return updatedEpochs[taskId];
    }

    public int getNextCompId(int degree, int entry) {
        if (degree < asyncThreshold) {
            return nextCompIds.asyncGet(entry);
        }
        else {
            return nextCompIds.get(entry);
        }
    }

    public final boolean update(int degree, int entry, int value) {
        int prev;
        if (degree < asyncThreshold) { // TODO : think about multiple ranges in a single sharedData
            prev = nextCompIds.asyncGet(entry);
            if (prev <= value) {
                return false;
            }
            nextCompIds.asyncSet(entry, value);
            return true;
        }
        else {
            do {
                prev = nextCompIds.get(entry);

                if (prev <= value) {
                    return false;
                }
            }
            while (!nextCompIds.compareAndSet(entry, prev, value));
            return true;
        }
    }

    public boolean compareCompIds(int prev, int value) {
        return prev > value;
    }

    public void setCurComponentId(int pos, int value) {
        curCompIds[pos] = value;
    }

    public int getCurCompId(int pos) {
        return curCompIds[pos];
    }

    public void reset() {
        initializeTable();
    }
}
