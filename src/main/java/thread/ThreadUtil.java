package thread;

public class ThreadUtil {
    public static Thread[] createAndStartThreads(int num, Runnable runnable) {
        Thread[] threads = new Thread[num];

        for (Thread thread : threads) {
            thread = new Thread(runnable);
            thread.start();
        }
        return threads;
    }
}


