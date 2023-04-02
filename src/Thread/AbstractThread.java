package Thread;

import Monitor.CounterMonitor;
import Monitor.BoundedListMonitor;
import Monitor.UmboundedListMonitor;

public abstract class AbstractThread extends Thread{
    protected UmboundedListMonitor filesToReadList;
    protected BoundedListMonitor sizeClassificationList;
    protected CounterMonitor[] counterList;

    protected int n;
    protected int maxl;
    protected int ni;

    protected final int maxCaracters = 10;
    protected final String commitSuicideMessage = "killyourself";
}
