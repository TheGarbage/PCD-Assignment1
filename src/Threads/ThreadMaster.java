package Threads;

import Data.DataMaster;
import Monitors.*;
import Threads.Controller.SizeCountersObeserver;
import Threads.Controller.RankingListObserver;
import Utilities.ThreadConstants;
import View.View;
import Utilities.StateEnum;

public class ThreadMaster extends Thread{

    final ThreadSlave[] threadArray;
    RankingListObserver rankingListObserver;
    SizeCountersObeserver sizeCountersObeserver;
    View myView;
    final DataMaster dataMaster = new DataMaster();
    final FilesToReadList filesToReadList;
    final StateMonitor stateMain;
    final StateMonitor stateSizeCountersObserver;
    final StateMonitor stateRankingListObserver;


    public ThreadMaster(){
        super();
        this.threadArray = new ThreadSlave[Runtime.getRuntime().availableProcessors()];
        for(int i = 0; i < threadArray.length; i++)
            threadArray[i] = new ThreadSlave(this.dataMaster);
        filesToReadList = dataMaster.getFilesToReadList();
        stateMain = dataMaster.getState();
        stateSizeCountersObserver = dataMaster.getCountersHasChanged();
        stateRankingListObserver = dataMaster.getListHasChanged();
        this.start();
    }

    @Override
    public void run(){
            try {
                this.myView = new View(dataMaster);
                this.myView.open();
                rankingListObserver = new RankingListObserver(myView, dataMaster);
                sizeCountersObeserver = new SizeCountersObeserver(myView, dataMaster);
                while(true) {
                    if(stateMain.readState() == StateEnum.START) {
                        long time = System.currentTimeMillis();
                        filesToReadList.put(dataMaster.getD()); // Inizia il lavoro
                        StateEnum stateEnum= stateMain.readState();
                        if(stateEnum == StateEnum.CONTINUE)
                            myView.setFinish("Fine, tempo: " + (System.currentTimeMillis() - time));
                        else {
                            filesToReadList.reset();
                            stateMain.readState();
                            filesToReadList.activate();
                            if(stateEnum == StateEnum.OFF)
                                break;
                        }
                    }
                    else
                        break;
                }
                for (Thread t : threadArray)
                    filesToReadList.put(ThreadConstants.TERMINATION_MESSAGE);
                stateRankingListObserver.changeState(StateEnum.OFF);
                stateSizeCountersObserver.changeState(StateEnum.OFF);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}