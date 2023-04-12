package Threads;

import Monitors.DataMonitor;
import Monitors.*;
import Threads.Observers.CountersObeserver;
import Threads.Observers.RankingListObserver;
import Utilities.ThreadConstants;
import View.View;
import Utilities.StateEnum;

import java.io.File;

public class ThreadMaster extends Thread{
    final int nSlavesThread = 2 * Runtime.getRuntime().availableProcessors();
    final ThreadSlave[] threadArray;
    RankingListObserver rankingListObserver;
    CountersObeserver countersObeserver;
    View myView;
    final DataMonitor dataMonitor = new DataMonitor();
    final PathsToReadListMonitor pathsToReadListMonitor;
    final StateMonitor stateMain;
    final StateMonitor countersObserverState;
    final StateMonitor rankingListObserverState;


    public ThreadMaster(){
        super();
        this.threadArray = new ThreadSlave[nSlavesThread];
        for(int i = 0; i < threadArray.length; i++)
            threadArray[i] = new ThreadSlave(this.dataMonitor);
        pathsToReadListMonitor = dataMonitor.getFilesToReadList();
        stateMain = dataMonitor.getMasterstate();
        countersObserverState = dataMonitor.getCountersObserverState();
        rankingListObserverState = dataMonitor.getRankingListObserverState();
        this.start();
    }

    @Override
    public void run(){
            try {
                this.myView = new View(dataMonitor);
                this.myView.openWindow();
                rankingListObserver = new RankingListObserver(myView, dataMonitor);
                countersObeserver = new CountersObeserver(myView, dataMonitor);
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
                            pathsToReadListMonitor.put(path); // Inizia il lavoro
                            stateEnum = stateMain.readState();
                            if (stateEnum == StateEnum.CONTINUE) {
                                myView.disableStop();
                                if (dataMonitor.sizeClassificationListIsEmpty())
                                    myView.setFinish(" No java files in the directory");
                                else
                                    myView.setFinish(" Time to finish: " + (System.currentTimeMillis() - time) + "ms");
                            }
                            else { // Only stop or off are possible (start button is still disable)
                                pathsToReadListMonitor.reset();
                                stateMain.readState();
                                pathsToReadListMonitor.activate();
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
                    pathsToReadListMonitor.put(ThreadConstants.TERMINATION_MESSAGE);
                for (Thread t : threadArray)
                    t.join();
                rankingListObserverState.changeState(StateEnum.OFF);
                rankingListObserver.join();
                countersObserverState.changeState(StateEnum.OFF);
                countersObeserver.join();
                System.out.println("All threads terminated");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}