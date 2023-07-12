package Threads.Observers;

import Monitors.DataMonitor;
import Monitors.StateMonitor;
import View.View;

import java.lang.reflect.InvocationTargetException;

public class CountersObeserver extends AbstractObserver{
    public CountersObeserver(View myView, DataMonitor dataMonitor){
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getCountersObserverState();
    }

    @Override
    void updateMyView() throws InterruptedException, InvocationTargetException {
        myView.setIntervalsText(this.dataManster.creaStringCounters());
    }
}
