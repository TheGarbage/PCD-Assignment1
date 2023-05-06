package Assignment2.Executor;

import Assignment2.Common.Interface.DataWrapper;
import Assignment2.Common.Interface.SourceAnalyser;
import Assignment2.Common.Monitors.DataMonitor;
import Assignment2.Common.Utilities.ThreadConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

public abstract class ExecutorSourceAnalyser implements SourceAnalyser {
    ExecutorService executor;
    DataMonitor dataMonitor;

    private Thread start(){
        executor = getExecutor();
        return Thread.ofVirtual().start(() -> {
            long startTime = System.currentTimeMillis();
            String finalMessage = "Error";
            String path = dataMonitor.getD();
            File[] directoryFiles = (new File(path).listFiles());
            try{
                if (directoryFiles == null)
                    finalMessage = " Invalid directory Selected";
                else if (directoryFiles.length == 0)
                    finalMessage = " The selected directory is empty";
                else {
                    executor.submit(() -> directoryReader(path)).get();
                    if (dataMonitor.sizeClassificationListIsEmpty())
                        finalMessage = " No java files in the directory";
                    else
                        finalMessage = " Time to finish: " + (System.currentTimeMillis() - startTime) + "ms";
                }
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException ignored){
            } finally {
                dataMonitor.setFinalMessage(finalMessage);
            }
        });
    }

    private void stop(){
        executor.shutdownNow();
    }

    private void linesCounter(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        String item = ThreadConstants.STRING_PREFIX.substring(0, ThreadConstants.MAX_DIGITS - String.valueOf(lines).length()) + lines + String.valueOf(lines).length() + (new File(path)).getName();
        dataMonitor.addFile(item, lines);
    }

    private void directoryReader(String path){
        try{
            File file = new File(path);
            ArrayList<Future> futures = new ArrayList<>();
            for (File f : file.listFiles())
                if (f.getName().endsWith(".java"))
                    futures.add(executor.submit(() -> {
                        try {
                            linesCounter(f.getPath());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }));
                else if (f.listFiles() != null && f.listFiles().length > 0)
                    futures.add(executor.submit(() -> directoryReader(f.getPath())));
            for(Future future : futures)
                future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }catch (InterruptedException | RejectedExecutionException | CancellationException ignored){}
    }

    @Override
    public void getReportpublic(String d, int n, int maxl, int ni) throws InterruptedException {
        dataMonitor = new DataMonitor(d, n, maxl, ni, this::stop);
        start().join();
        System.out.println("Ranking: \n" + dataMonitor.makeStringList());
        System.out.println("Intervals: \n" + dataMonitor.makeStringCounters());
        System.out.println("Total Time: \n" + dataMonitor.getFinalMessage());
    }

    @Override
    public DataWrapper analyzeSources(String d, int n, int maxl, int ni) {
        dataMonitor = new DataMonitor(d, n, maxl, ni, this::stop);
        start();
        return dataMonitor;
    }

    public abstract ExecutorService getExecutor();
}
