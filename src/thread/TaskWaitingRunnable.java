package thread;

import task.Task;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskWaitingRunnable implements Runnable
{
    LinkedBlockingQueue<Task> taskQueue;

    public TaskWaitingRunnable(LinkedBlockingQueue<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            Task task = null;
            try {
                task = taskQueue.take();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.run();
        }
    }
}
