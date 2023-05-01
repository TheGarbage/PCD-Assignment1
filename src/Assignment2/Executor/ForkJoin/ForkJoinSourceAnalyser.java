package Assignment2.Executor.ForkJoin;

import Assignment2.Executor.ExecutorSourceAnalyser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinSourceAnalyser extends ExecutorSourceAnalyser {
    @Override
    public ExecutorService getExecutor() {
        return new ForkJoinPool();
    }
}
