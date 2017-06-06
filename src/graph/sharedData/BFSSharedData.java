package graph.sharedData;

public class BFSSharedData {
    int[] levels;
    int[] taskLevels;
    volatile int bfsCurrentLevel;

    final int nodeCapacity;
    final int numTasks;

    public BFSSharedData(int nodeCapacity, int numTasks) {
        this.nodeCapacity = nodeCapacity;
        this.numTasks = numTasks;
    }

    public final void initializeTable() {
        levels = new int[nodeCapacity];
        taskLevels = new int[numTasks];
        bfsCurrentLevel = 0;
    }

    public final void setVertexValue(int entry, int value) {
        levels[entry] = value;
    }

    public final int getVertexValue(int entry) {
        return levels[entry];
    }

    public final void setTaskLevels(int taskId, int value) {
        if (taskLevels[taskId] != value) {
            taskLevels[taskId] = value;
        }
    }

    public boolean checkTaskLevels(int taskId, int level) {
        return taskLevels[taskId] >= level;
    }

    public final void incrementBFSLevel() {
        bfsCurrentLevel++;
    }

    public final int getCurrentBFSLevel() {
        return bfsCurrentLevel;
    }

    public void setCurrentBFSLevel(int level) {
        bfsCurrentLevel = level;
    }

    public void reset() {
        initializeTable();
    }
}

