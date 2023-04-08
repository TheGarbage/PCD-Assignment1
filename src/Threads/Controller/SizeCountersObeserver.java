package Threads.Controller;

import Monitors.DataMonitor;
import Monitors.StateMonitor;
import View.View;

import java.lang.reflect.InvocationTargetException;

public class SizeCountersObeserver extends AbstractObserver{
    public SizeCountersObeserver(View myView, DataMonitor dataMonitor){
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getCountersHasChanged();
    }

    @Override
    void updateMyView() throws InterruptedException, InvocationTargetException {
        myView.setIntervalsText(this.dataManster.creaStringCounters());
    }
}
