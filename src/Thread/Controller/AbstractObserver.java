package Thread.Controller;

import Monitor.DataMonitor;
import Monitor.StateMonitor;
import View.View;

public abstract class AbstractObserver extends Thread{
    final long timeForSeconds = 30;
    final long interval = 1000 / timeForSeconds;
    final StateMonitor stateMonitor;
    final View myView;
    final DataMonitor dataMonitor;

    public AbstractObserver(View myView, DataMonitor dataMonitor){
        this.myView = myView;
        this.dataMonitor = dataMonitor;
        this.stateMonitor = getStateMonitor();
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            long startTime = System.currentTimeMillis();
            long duration = System.currentTimeMillis() - startTime;
            try {
                if(stateMonitor.readState() == StateMonitor.StateEnum.START)
                    updateMyView();
                else
                    return;
            } catch (InterruptedException e) {
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
    abstract void updateMyView() throws InterruptedException;
}
