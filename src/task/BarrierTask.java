package task;

import graph.GraphAlgorithmInterface;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarrierTask implements GraphAlgorithmInterface {
    CyclicBarrier barriers;
    
    public BarrierTask(CyclicBarrier barrier) {
        this.barriers = barrier;
    }
    
    @Override
    public void execute() {
        try {
            barriers.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {

        }
    }
    
    @Override
    public void reset() {
        
    }
}
