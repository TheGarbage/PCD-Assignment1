package Monitors;
import Utilities.StateEnum;
import Utilities.ThreadConstants;

import java.util.ArrayList;

public class PathsToReadList {
    final ArrayList<String> list = new ArrayList<>();
    CounterMonitor nActiveSlaveThread = new CounterMonitor();
    final StateMonitor stateMainThread;
    private boolean active = true;

    public PathsToReadList(StateMonitor stateMainThread){
        this.stateMainThread = stateMainThread;
    }

    public synchronized void put(String item) throws InterruptedException {
        if(active) {
            this.list.add(item);
            this.notify();
        }
    }

    public synchronized String get() throws InterruptedException {
        while (list.isEmpty()) wait();
        nActiveSlaveThread.increment();
        return this.list.remove(0);
    }

    public synchronized void decrementActiveMonitorCounter(){
        if(nActiveSlaveThread.decrement() == 0 && this.list.isEmpty())
            stateMainThread.changeState(StateEnum.CONTINUE);
    }

    public synchronized void reset(){
        list.clear();
        list.add(ThreadConstants.SKIP_MESSAGE); // Cosi ho la certezza che almeno uno attivo ci sarà
        active = false;
    }

    public synchronized void activate(){
        active = true;
    }
}
