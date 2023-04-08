package Threads;

import Monitors.DataMonitor;
import Monitors.*;
import Threads.Controller.SizeCountersObeserver;
import Threads.Controller.RankingListObserver;
import Utilities.ThreadConstants;
import View.View;
import Utilities.StateEnum;

import java.io.File;

public class ThreadMaster extends Thread{
    final int nSlavesThread = Runtime.getRuntime().availableProcessors();
    final ThreadSlave[] threadArray;
    RankingListObserver rankingListObserver;
    SizeCountersObeserver sizeCountersObeserver;
    View myView;
    final DataMonitor dataMonitor = new DataMonitor();
    final FilesToReadList filesToReadList;
    final StateMonitor stateMain;
    final StateMonitor stateSizeCountersObserver;
    final StateMonitor stateRankingListObserver;


    public ThreadMaster(){
        super();
        this.threadArray = new ThreadSlave[nSlavesThread];
        for(int i = 0; i < threadArray.length; i++)
            threadArray[i] = new ThreadSlave(this.dataMonitor);
        filesToReadList = dataMonitor.getFilesToReadList();
        stateMain = dataMonitor.getState();
        stateSizeCountersObserver = dataMonitor.getCountersHasChanged();
        stateRankingListObserver = dataMonitor.getListHasChanged();
        this.start();
    }

    @Override
    public void run(){
            try {
                this.myView = new View(dataMonitor);
                this.myView.openWindow();
                rankingListObserver = new RankingListObserver(myView, dataMonitor);
                sizeCountersObeserver = new SizeCountersObeserver(myView, dataMonitor);
                StateEnum stateEnum;
                while(true) {
                    stateEnum = stateMain.readState();
                    if(stateEnum == StateEnum.START) {
                        long time = System.currentTimeMillis();
                        String path = dataMonitor.getD();
                        File[] directoryFiles = (new File(path).listFiles());
                        if (directoryFiles == null)
                            myView.setFinish(" Invalid directory Selected");
                        else if(directoryFiles.length == 0)
                            myView.setFinish(" The selected directory is empty");
                        else {
                            myView.setProcessing();
                            filesToReadList.put(path); // Inizia il lavoro
                            stateEnum = stateMain.readState();
                            if (stateEnum == StateEnum.CONTINUE) {
                                myView.disableStop();
                                if (dataMonitor.sizeClassificationListIsEmpty())
                                    myView.setFinish(" No java files in the directory");
                                else
                                    myView.setFinish(" Time to finish: " + (System.currentTimeMillis() - time));
                            }
                            else { // Only stop or off is possible (start button is still disable)
                                filesToReadList.reset();
                                stateMain.readState();
                                filesToReadList.activate();
                                if (stateEnum == StateEnum.OFF)
                                    break;
                                myView.setFinish(" Stopped");
                            }
                        }
                    }
                    else if(stateEnum == StateEnum.STOP)
                        myView.setFinish(" Stopped after termination");
                    else
                        break;
                }
                for(int i = 0; i < nSlavesThread; i++)
                    filesToReadList.put(ThreadConstants.TERMINATION_MESSAGE);
                for (Thread t : threadArray)
                    t.join();
                stateRankingListObserver.changeState(StateEnum.OFF);
                rankingListObserver.join();
                stateSizeCountersObserver.changeState(StateEnum.OFF);
                sizeCountersObeserver.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}