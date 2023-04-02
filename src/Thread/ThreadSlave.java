package Thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ThreadSlave extends AbstractThread {
    ThreadMaster threadMaster;
    final String cod;

    public ThreadSlave(ThreadMaster threadMaster){
        this.threadMaster = threadMaster;
        this.cod = "0000000000";
    }

    public void inizializza(){
        this.sizeClassificationList = threadMaster.sizeClassificationList;
        this.counterList = threadMaster.counterList;
        this.n = threadMaster.n;
        this.maxl = threadMaster.maxl;
        this.ni = threadMaster.ni;
    }

    @Override
    public void run() {
        this.filesToReadList = threadMaster.filesToReadList;

        File file;
        long lines;
        String item;
        String path;

        while(true) {
            try {
                path = this.filesToReadList.get();
                if (path.equals(commitSuicideMessage)) break;
                else if(!path.equals("Pass")) {
                    if (path.endsWith(".java")) {
                        BufferedReader reader = new BufferedReader(new FileReader(path));
                        lines = 0;
                        while (reader.readLine() != null) lines++;
                        reader.close();
                        item = cod.substring(0, maxCaracters - String.valueOf(lines).length()) + lines + String.valueOf(lines).length() + path;
                        sizeClassificationList.put(item);
                        if (lines < this.maxl)
                            this.counterList[(int) lines / (this.maxl / (this.ni - 1))].increment();
                        else
                            this.counterList[this.ni - 1].increment();
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
