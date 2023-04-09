package Threads.Controller;

import Monitors.DataMonitor;
import Monitors.StateMonitor;
import View.View;
import Utilities.StateEnum;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractObserver extends Thread{
    final long timeForSeconds = 30;
    final long interval = 1000 / timeForSeconds;
    final StateMonitor stateMonitor;
    final View myView;
    final DataMonitor dataManster;

    public AbstractObserver(View myView, DataMonitor dataManster){
        this.myView = myView;
        this.dataManster = dataManster;
        this.stateMonitor = getStateMonitor();
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            long startTime = System.currentTimeMillis();
            try {
                if(stateMonitor.readState() == StateEnum.START)
                    updateMyView();
                else
                    return;
            } catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            long timeDifference = System.currentTimeMillis() - startTime;
            if (timeDifference < interval) {
                try {
                    sleep(interval - timeDifference);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    abstract StateMonitor getStateMonitor();
    abstract void updateMyView() throws InterruptedException, InvocationTargetException;
}
