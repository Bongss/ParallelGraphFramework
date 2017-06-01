package task;

import graph.GraphAlgorithmInterface;

public class Task {
	GraphAlgorithmInterface algorithm;

    public Task(GraphAlgorithmInterface algorithm) {
		this.algorithm = algorithm;
    }

    // For JIT Compiler
    public void reset() {
		algorithm.reset();
	}
	
    public void run() {
		algorithm.execute();
    }
}


