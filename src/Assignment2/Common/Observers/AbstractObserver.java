package Assignment2.Common.Observers;

import Assignment2.Common.Interface.DataWrapper;
import Assignment2.Common.Monitors.StateMonitor;
import Assignment2.Common.Utilities.StateEnum;
import Assignment2.Common.Utilities.ThreadConstants;
import Assignment2.Common.View.View;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractObserver extends Thread{
    final long targetTimeDifference = 1000 / ThreadConstants.timeForSecondsGuiUpdate;
    final StateMonitor stateMonitor;
    final View myView;
    final DataWrapper dataManster;

    public AbstractObserver(View myView, DataWrapper dataManster){
        this.myView = myView;
        this.dataManster = dataManster;
        this.stateMonitor = getStateMonitor();
        start();
    }

    public void run() {
        while (true) {
            long startTime = System.currentTimeMillis();
            try {
                if (stateMonitor.readState() == StateEnum.START)
                    updateMyView();
                else
                    return;
            } catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            long timeDifference = System.currentTimeMillis() - startTime;
            if (timeDifference < targetTimeDifference) {
                try {
                    Thread.sleep(targetTimeDifference - timeDifference);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    abstract StateMonitor getStateMonitor();
    abstract void updateMyView() throws InterruptedException, InvocationTargetException;
}
