package Assignment1.Threads.Observers;

import Assignment1.Monitors.DataMonitor;
import Assignment1.Monitors.StateMonitor;
import Assignment1.Utilities.ThreadConstants;
import Assignment1.View.View;
import Assignment1.Utilities.StateEnum;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractObserver extends Thread{
    final long targetTimeDifference = 1000 / ThreadConstants.timeForSecondsGuiUpdate;
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
            if (timeDifference < targetTimeDifference) {
                try {
                    sleep(targetTimeDifference - timeDifference);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    abstract StateMonitor getStateMonitor();
    abstract void updateMyView() throws InterruptedException, InvocationTargetException;
}
