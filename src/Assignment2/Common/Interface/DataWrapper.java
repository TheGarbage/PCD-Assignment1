package Assignment2.Common.Interface;

import Assignment2.Common.Monitors.StateMonitor;

public interface DataWrapper {

    StateMonitor getProcessObserverState();

    String getFinalMessage();

    StateMonitor getRankingListObserverState();

    String makeStringList() throws InterruptedException;

    StateMonitor getCountersObserverState();

    String makeStringCounters();

    void stop();
}
