package Thread;

import Monitor.CounterMonitor;
import Monitor.SizeClassificationList;
import Monitor.FilesToReadList;

public abstract class AbstractThread extends Thread{
    protected FilesToReadList filesToReadList;
    protected SizeClassificationList sizeClassificationList;
    protected CounterMonitor[] counterList;

    protected int n;
    protected int maxl;
    protected int ni;

    protected final int maxCaracters = 10;
    protected final String commitSuicideMessage = "killyourself";
}
