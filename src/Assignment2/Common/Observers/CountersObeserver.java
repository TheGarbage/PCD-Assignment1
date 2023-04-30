package Assignment2.Common.Observers;

import Assignment2.Common.Interface.DataWrapper;
import Assignment2.Common.Monitors.StateMonitor;
import Assignment2.Common.View.View;

import java.lang.reflect.InvocationTargetException;

public class CountersObeserver extends AbstractObserver {
    public CountersObeserver(View myView, DataWrapper dataMonitor){
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getCountersObserverState();
    }

    @Override
    void updateMyView() throws InterruptedException, InvocationTargetException {
        myView.setIntervalsText(this.dataManster.makeStringCounters());
    }
}
