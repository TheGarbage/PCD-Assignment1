package Monitors;
import Utilities.StateEnum;
import Utilities.ThreadConstants;

import java.util.ArrayList;

public class PathsToReadListMonitor {
    final ArrayList<String> PathsToReadList = new ArrayList<>();
    int nActiveSlaveThread = 0;
    final StateMonitor stateMainThread;
    private boolean active = true;

    public PathsToReadListMonitor(StateMonitor stateMainThread){
        this.stateMainThread = stateMainThread;
    }

    public synchronized void put(String item) throws InterruptedException {
        if(active) {
            this.PathsToReadList.add(item);
            this.notify();
        }
    }

    public synchronized String get() throws InterruptedException {
        while (PathsToReadList.isEmpty()) wait();
        nActiveSlaveThread++;
        return this.PathsToReadList.remove(0);
    }

    public synchronized void decrementActiveMonitorCounter(){
        if(--nActiveSlaveThread == 0 && this.PathsToReadList.isEmpty())
            stateMainThread.changeState(StateEnum.CONTINUE);
    }

    public synchronized void reset(){
        PathsToReadList.clear();
        PathsToReadList.add(ThreadConstants.SKIP_MESSAGE); // Cosi ho la certezza che almeno uno attivo ci sarà
        active = false;
    }

    public synchronized void activate(){
        active = true;
    }
}
