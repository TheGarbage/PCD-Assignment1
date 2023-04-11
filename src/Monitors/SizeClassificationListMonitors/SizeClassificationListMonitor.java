package Monitors.SizeClassificationListMonitors;

import java.util.ArrayList;

public interface SizeClassificationListMonitor {
    boolean put(String item);
    ArrayList<String> read() throws InterruptedException;
    boolean isEmpty();
}
