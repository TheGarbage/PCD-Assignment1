package Assignment2.Common.Observers;

import Assignment2.Common.Interface.DataWrapper;
import Assignment2.Common.Monitors.StateMonitor;
import Assignment2.Common.View.View;

public class ProcessObservers extends AbstractObserver{
    public ProcessObservers(View myView, DataWrapper dataMonitor){
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getProcessObserverState();
    }

    @Override
    void updateMyView(){
        myView.setFinish(this.dataManster.getFinalMessage());
    }
}
