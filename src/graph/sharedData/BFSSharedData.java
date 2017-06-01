package graph.sharedData;

import atomic.AtomicIntegerArray;

import java.util.Arrays;
import java.util.function.IntBinaryOperator;

public class BFSSharedData
{
    public static IntBinaryOperator updateFunction;

    public static void setUpdateFunction(IntBinaryOperator function) {
        updateFunction = function;
    }

    AtomicIntegerArray table;
    int[] activeNodeCheckArray;

    volatile int partitionActiveValue;

    final int nodeCapacity;
    final int asyncThreshold;

    public BFSSharedData(int nodeCapacity, int asyncThreshold) {
        this.nodeCapacity = nodeCapacity;
        this.asyncThreshold = asyncThreshold;

    }

    public final void initializeTable() {
        table = new AtomicIntegerArray(nodeCapacity);
        activeNodeCheckArray = new int[nodeCapacity];

        Arrays.fill(activeNodeCheckArray, -1);
    }

    public final void setVertexValue(int entry, int value) {
        if (entry < asyncThreshold) {
            table.asyncSet(entry, value);
        }
        else {
            table.set(entry, value);
        }
    }

    public final int getVertexValue(int entry) {
        if (entry < asyncThreshold) {
            return table.asyncGet(entry);
        }
        else {
            return table.get(entry);
        }
    }

    public final void update(int entry, int value) {
        if (entry < asyncThreshold) { // TODO : think about multiple ranges in a single sharedData
            table.asyncGetAndAccumulate(entry, value, updateFunction);
        }
        else {
            table.getAndAccumulate(entry, value, updateFunction);
        }
    }

    public final void setPartitionActiveValue(int value) {
        if (partitionActiveValue != value) {
            partitionActiveValue = value;
        }
    }

    public boolean checkPartitionIsActive(int compareValue) {
        return partitionActiveValue == compareValue;
    }

    public void reset() {
        initializeTable();
        partitionActiveValue = 0;
    }
}

