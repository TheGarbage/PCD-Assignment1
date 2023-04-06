package Thread.Controller;

import Monitor.DataMonitor;
import Monitor.StateMonitor;
import View.View;

public class SizeCountersObeserver extends AbstractObserver{
    public SizeCountersObeserver(View myView, DataMonitor dataMonitor){
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataMonitor.getCountersHasChanged();
    }

    @Override
    void updateMyView(){
        myView.setCountersText(this.dataMonitor.creaStringCounters());
    }
}
