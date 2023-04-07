package Threads.Controller;

import Data.DataMaster;
import Monitors.StateMonitor;
import View.View;
import Utilities.StateEnum;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractObserver extends Thread{
    final long timeForSeconds = 30;
    final long interval = 1000 / timeForSeconds;
    final StateMonitor stateMonitor;
    final View myView;
    final DataMaster dataManster;

    public AbstractObserver(View myView, DataMaster dataManster){
        this.myView = myView;
        this.dataManster = dataManster;
        this.stateMonitor = getStateMonitor();
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            long startTime = System.currentTimeMillis();
            long duration = System.currentTimeMillis() - startTime;
            try {
                if(stateMonitor.readState() == StateEnum.START)
                    updateMyView();
                else
                    return;
            } catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if (duration < interval) {
                try {
                    sleep(interval - duration);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    abstract StateMonitor getStateMonitor();
    abstract void updateMyView() throws InterruptedException, InvocationTargetException;
}
