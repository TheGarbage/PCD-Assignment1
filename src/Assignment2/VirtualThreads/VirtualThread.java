package Assignment2.VirtualThreads;

import Assignment2.Common.Interface.SourceAnalyser;
import Assignment2.Common.Utilities.ThreadConstants;
import Assignment2.Common.Monitors.DataMonitor;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VirtualThread  implements SourceAnalyser {
    final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    DataMonitor dataMonitor;

    public DataMonitor analyzeSources(String d, int n, int maxl, int ni){
        dataMonitor = new DataMonitor(d, n, maxl, ni, this::stop);
        Thread.ofVirtual().start(() -> {
            Long startTime = System.currentTimeMillis();
            String finalMessage = "Error";
            String path = dataMonitor.getD();
            File[] directoryFiles = (new File(path).listFiles());
            if (directoryFiles == null)
                finalMessage = " Invalid directory Selected";
            else if (directoryFiles.length == 0)
                finalMessage = " The selected directory is empty";
            else {
                executor.submit(() -> directoryReader(path));
            }
            try{
                if(executor.awaitTermination(10, TimeUnit.MINUTES))
                    if(executor.isTerminated())
                        finalMessage = " Stopped";
                    else if (dataMonitor.sizeClassificationListIsEmpty())
                        finalMessage = " No java files in the directory";
                    else
                        finalMessage = " Time to finish: " + (System.currentTimeMillis() - startTime) + "ms";
                else
                    finalMessage = " Max execution time (10 minutes) reached";

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                dataMonitor.setFinalMessage(finalMessage);
            }
        });
        return dataMonitor;
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
        dataMonitor.addFile(item, lines);
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
