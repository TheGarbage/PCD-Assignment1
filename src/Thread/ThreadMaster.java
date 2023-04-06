package Thread;

import Monitor.*;
import Thread.Controller.SizeCountersObeserver;
import Thread.Controller.RankingListObserver;
import View.View;

public class ThreadMaster extends Thread{

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
        this.threadArray = new ThreadSlave[Runtime.getRuntime().availableProcessors()];
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
                this.myView.open();
                rankingListObserver = new RankingListObserver(myView, dataMonitor);
                sizeCountersObeserver = new SizeCountersObeserver(myView, dataMonitor);
                while(true) {
                    if(stateMain.readState() == StateMonitor.StateEnum.START) {
                        long time = System.currentTimeMillis();
                        filesToReadList.put(dataMonitor.getD()); // Inizia il lavoro
                        StateMonitor.StateEnum stateEnum= stateMain.readState();
                        if(stateEnum == StateMonitor.StateEnum.CONTINUE)
                            myView.setFinish("Fine, tempo: " + (System.currentTimeMillis() - time));
                        else {
                            filesToReadList.reset();
                            stateMain.readState();
                            filesToReadList.activate();
                            if(stateEnum == StateMonitor.StateEnum.OFF)
                                break;
                        }
                    }
                    else
                        break;
                }
                for (Thread t : threadArray)
                    filesToReadList.put(DataMonitor.terminationMessage);
                stateRankingListObserver.changeState(StateMonitor.StateEnum.OFF);
                stateSizeCountersObserver.changeState(StateMonitor.StateEnum.OFF);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}