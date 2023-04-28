package Assignment1.Threads.Observers;

import Assignment1.Monitors.DataMonitor;
import Assignment1.Monitors.StateMonitor;
import Assignment1.View.View;

import java.lang.reflect.InvocationTargetException;

public class RankingListObserver extends AbstractObserver{
    public RankingListObserver(View myView, DataMonitor dataMonitor) {
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getRankingListObserverState();
    }

    @Override
    void updateMyView() throws InterruptedException, InvocationTargetException {
        myView.setRankingText(this.dataManster.creaStringList());
    }
}
