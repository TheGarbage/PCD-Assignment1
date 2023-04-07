package Threads.Controller;

import Data.DataMaster;
import Monitors.StateMonitor;
import View.View;

import java.lang.reflect.InvocationTargetException;

public class SizeCountersObeserver extends AbstractObserver{
    public SizeCountersObeserver(View myView, DataMaster dataMaster){
        super(myView, dataMaster);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getCountersHasChanged();
    }

    @Override
    void updateMyView() throws InterruptedException, InvocationTargetException {
        myView.setCountersText(this.dataManster.creaStringCounters());
    }
}
