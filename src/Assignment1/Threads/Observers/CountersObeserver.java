package Assignment1.Threads.Observers;

import Assignment1.Monitors.DataMonitor;
import Assignment1.Monitors.StateMonitor;
import Assignment1.View.View;

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
