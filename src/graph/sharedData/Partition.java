//package graph.partition;
//
//public abstract class Partition
//{
//    final int asyncRangeSize;
//    int partitionId;
//    int partitionSize;
//    int[] activeNodeCheckArray;
//
//    volatile int partitionActiveValue;
//
//    Partition(int partitionId, int maxNodeId, int partitionSize, int asyncRangeSize) {
//        this.partitionId = partitionId;
//        this.partitionSize = partitionSize;
//        this.partitionActiveValue = 0;
//        if ((partitionId + 1) * this.partitionSize > maxNodeId) {
//            this.partitionSize = (maxNodeId % partitionSize) + 1;
//        }
//        this.asyncRangeSize = asyncRangeSize;
//        initializeTable();
//    }
//
//    public abstract void initializeTable();
//
//    public int getSize() {
//        return partitionSize;
//    }
//
//    public final void setPartitionActiveValue(int value) {
//        if (partitionActiveValue != value) {
//            partitionActiveValue = value;
//        }
//    }
//
//    public boolean checkPartitionIsActive(int compareValue) {
//        return partitionActiveValue == compareValue;
//    }
//
//    public void reset() {
//        initializeTable();
//        partitionActiveValue = 0;
//    }
//}
