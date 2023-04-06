package Thread.Controller;

import Monitor.DataMonitor;
import Monitor.StateMonitor;
import View.View;

public class RankingListObserver extends AbstractObserver{
    public RankingListObserver(View myView, DataMonitor dataMonitor) {
        super(myView, dataMonitor);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataMonitor.getListHasChanged();
    }

    @Override
    void updateMyView() throws InterruptedException {
        myView.setListText(this.dataMonitor.creaStringList());
    }
}
