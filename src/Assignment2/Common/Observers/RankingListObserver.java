package Assignment2.Common.Observers;

import Assignment2.Common.Monitors.DataMonitor;
import Assignment2.Common.Monitors.StateMonitor;
import Assignment2.Common.View.View;

import java.lang.reflect.InvocationTargetException;

public class RankingListObserver extends AbstractObserver {
    public RankingListObserver(View myView, DataMonitor dataMonitor) {
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getRankingListObserverState();
    }

    @Override
    void updateMyView() throws InterruptedException, InvocationTargetException {
        myView.setRankingText(this.dataManster.makeStringList());
    }
}
