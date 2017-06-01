package graph.sharedData;

import atomic.AtomicDoubleArray;

import java.util.function.DoubleBinaryOperator;

public class PageRankSharedData
{
    public static DoubleBinaryOperator updateFunction;

    public static void setUpdateFunction(DoubleBinaryOperator function)
    {
        updateFunction = function;
    }

    AtomicDoubleArray[] tables;

    int tablePos = 0;
    final int nodeCapacity;
    final int asyncThreshold;

    public PageRankSharedData(int nodeCapacity, int asyncThreshold)
    {
        this.nodeCapacity = nodeCapacity;
        this.asyncThreshold = asyncThreshold;
    }

    public final void initializeTable()
    {
        tables = new AtomicDoubleArray[2];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = new AtomicDoubleArray(nodeCapacity);
        }
    }

    public final void initializedCallback()
    {
        swapConsecutiveTwoTables();
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
