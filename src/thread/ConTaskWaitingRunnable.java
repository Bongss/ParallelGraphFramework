package thread;

import task.Task;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConTaskWaitingRunnable implements Runnable
{
    ConcurrentLinkedQueue<Task> taskQueue;

    public ConTaskWaitingRunnable(ConcurrentLinkedQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        Task task = null;
        while (true) {
            task = taskQueue.poll();
            if (task == null) {
                continue;
            }
            task.run();
        }
    }
}
