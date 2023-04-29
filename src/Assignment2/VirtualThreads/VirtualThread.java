package Assignment2.VirtualThreads;

import Assignment2.Common.Utilities.ThreadConstants;
import Assignment2.Common.Monitors.DataMonitor;
import Assignment2.Common.Monitors.StateMonitor;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThread {
    final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    final DataMonitor dataMonitor = new DataMonitor();
    final StateMonitor processState;
    final StateMonitor countersObserverState;
    final StateMonitor rankingListObserverState;
    final DataMonitor dataManster;
    long startTime;


    public VirtualThread(){
        processState = dataMonitor.getProcesState();
        countersObserverState = dataMonitor.getCountersObserverState();
        rankingListObserverState = dataMonitor.getRankingListObserverState();
        dataManster = new DataMonitor();
    }

    public void start(){
        startTime = System.currentTimeMillis();
        String path = dataMonitor.getD();
        File[] directoryFiles = (new File(path).listFiles());
        if (directoryFiles == null);
            // Invalid directory Selected
        else if(directoryFiles.length == 0);
            // The selected directory is empty
        else {
            executor.submit(() -> directoryReader(path));
        }
        executor.isTerminated();
    }

    public void stop(){
        executor.shutdown();
    }

    public void linesCounter(String path) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        String item = ThreadConstants.STRING_PREFIX.substring(0, ThreadConstants.MAX_DIGITS - String.valueOf(lines).length()) + lines + String.valueOf(lines).length() + (new File(path)).getName();
        dataManster.addFile(item, lines);
    }

    public void directoryReader(String path){
        File file = new File(path);
        for (File f : file.listFiles()) {
            if (f.getName().endsWith(".java"))
                executor.submit(() -> {
                    try {
                        linesCounter(f.getPath());
                    } catch ( IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            else if (f.listFiles() != null && f.listFiles().length > 0)
                executor.submit(() -> directoryReader(f.getPath()));
        }
    }
}
