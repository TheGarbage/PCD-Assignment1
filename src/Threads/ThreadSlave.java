package Threads;

import Monitors.DataMonitor;
import Monitors.FilesToReadList;
import Utilities.ThreadConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ThreadSlave extends Thread{
    DataMonitor dataManster;
    final FilesToReadList filesToReadList;

    public ThreadSlave(DataMonitor dataManster){
        this.dataManster = dataManster;
        this.filesToReadList = dataManster.getFilesToReadList();
        this.start();
    }

    @Override
    public void run() {
        File file;
        int lines;
        String item;
        String path;

        while(true) {
            try {
                path = this.filesToReadList.get();
                if (path.equals(ThreadConstants.TERMINATION_MESSAGE)) break;
                else if(!path.equals(ThreadConstants.SKIP_MESSAGE)) {
                    if (path.endsWith(".java")) {
                        BufferedReader reader = new BufferedReader(new FileReader(path));
                        lines = 0;
                        while (reader.readLine() != null) lines++;
                        reader.close();
                        item = ThreadConstants.STRING_PREFIX.substring(0, ThreadConstants.MAX_DIGITS - String.valueOf(lines).length()) + lines + String.valueOf(lines).length() + (new File(path)).getName();
                        dataManster.aggiungiFile(item, lines);
                    }
                    else {
                        file = new File(path);
                        for (File f : file.listFiles())
                            if (f.getName().endsWith(".java") || (f.listFiles() != null && f.listFiles().length > 0))
                                this.filesToReadList.put(f.getPath());
                    }
                }
                this.filesToReadList.decrementActiveMonitorCounter();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
