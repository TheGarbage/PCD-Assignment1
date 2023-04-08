package Threads.Controller;

import Monitors.DataMonitor;
import Monitors.StateMonitor;
import View.View;

import java.lang.reflect.InvocationTargetException;

public class RankingListObserver extends AbstractObserver{
    public RankingListObserver(View myView, DataMonitor dataMonitor) {
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getListHasChanged();
    }

    @Override
    void updateMyView() throws InterruptedException, InvocationTargetException {
        myView.setRankingText(this.dataManster.creaStringList());
    }
}
