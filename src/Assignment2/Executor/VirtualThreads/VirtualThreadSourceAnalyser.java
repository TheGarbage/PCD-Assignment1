package Assignment2.Executor.VirtualThreads;

import Assignment2.Executor.ExecutorSourceAnalyser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadSourceAnalyser extends ExecutorSourceAnalyser {

    @Override
    public ExecutorService getExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
