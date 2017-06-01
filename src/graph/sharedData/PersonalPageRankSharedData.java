package graph.sharedData;

import atomic.AtomicDoubleArray;

import java.util.function.DoubleBinaryOperator;

public class PersonalPageRankSharedData
{
    public static DoubleBinaryOperator updateFunction;

    public static void setUpdateFunction(DoubleBinaryOperator function)
    {
        updateFunction = function;
    }

    AtomicDoubleArray[] tables;
    byte[] seedCheckArray;

    final int nodeCapacity;
    final int asyncThreshold;
    int tablePos = 0;

    public PersonalPageRankSharedData(int nodeCapacity, int asyncThreshold)
    {
        this.nodeCapacity = nodeCapacity;
        this.asyncThreshold = asyncThreshold;
    }

    public final void initializeTable()
    {
        seedCheckArray = new byte[nodeCapacity];
        tables = new AtomicDoubleArray[2];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = new AtomicDoubleArray(nodeCapacity);
        }

        for (int i = 0; i < tables.length; i++) {
            for (int j = 0; j < nodeCapacity; j++) {
                tables[i].set(j, 0);
            }
        }
    }

    public final void initializedCallback()
    {
        swapConsecutiveTwoTables();
    }

    public void setSeedNode(int pos)
    {
        seedCheckArray[pos] = 1;
    }

    public boolean isNodeSeed(int pos)
    {
        return seedCheckArray[pos] == 1;
    }

    public final void setVertexValue(int entry, double value)
    {
        tables[tablePos].asyncSet(entry, value);
    }

    public final void setNextVertexValue(int entry, double value)
    {
        tables[tablePos + 1].asyncSet(entry, value);
    }

    public final double getVertexValue(int entry)
    {
        return tables[tablePos].asyncGet(entry);
    }

    public final void asyncUpdateNextTable(int entry, double value)
    {
        tables[1].asyncGetAndAccumulate(entry, value, updateFunction);
    }

    public final void atomicUpdateNextTable(int entry, double value)
    {
        tables[1].getAndAccumulate(entry, value, updateFunction);
    }

    public final void swapConsecutiveTwoTables()
    {
        AtomicDoubleArray tmp = tables[tablePos];
        tables[tablePos] = tables[tablePos + 1];
        tables[tablePos + 1] = tmp;
    }

    public void reset()
    {
        initializeTable();
    }
}
