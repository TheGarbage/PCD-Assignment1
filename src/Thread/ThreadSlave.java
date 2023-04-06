package Thread;

import Monitor.DataMonitor;
import Monitor.FilesToReadList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ThreadSlave extends Thread{
    DataMonitor dataMonitor;
    final FilesToReadList filesToReadList;

    public ThreadSlave(DataMonitor dataMonitor){
        this.dataMonitor = dataMonitor;
        this.filesToReadList = dataMonitor.getFilesToReadList();
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
                if (path.equals(DataMonitor.terminationMessage)) break;
                else if(!path.equals(DataMonitor.skipMessagge)) {
                    if (path.endsWith(".java")) {
                        BufferedReader reader = new BufferedReader(new FileReader(path));
                        lines = 0;
                        while (reader.readLine() != null) lines++;
                        reader.close();
                        item = DataMonitor.stringPrefix.substring(0, DataMonitor.maxCaracters - String.valueOf(lines).length()) + lines + String.valueOf(lines).length() + (new File(path)).getName();
                        dataMonitor.aggiungiFile(item, lines);
                    }
                    else {
                        file = new File(path);
                        for (File f : file.listFiles())
                            if (f.getName().endsWith(".java") || f.listFiles() != null)
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
