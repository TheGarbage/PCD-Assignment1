package Threads.Controller;

import Data.DataMaster;
import Monitors.StateMonitor;
import View.View;

public class RankingListObserver extends AbstractObserver{
    public RankingListObserver(View myView, DataMaster dataMaster) {
        super(myView, dataMaster);
    }

    @Override
    StateMonitor getStateMonitor() {
        return this.dataManster.getListHasChanged();
    }

    @Override
    void updateMyView() throws InterruptedException {
        myView.setListText(this.dataManster.creaStringList());
    }
}
