package Monitors.RankingListMonitor;

import java.util.ArrayList;

public interface RankingMonitor {
    boolean put(String item);
    ArrayList<String> read() throws InterruptedException;
    boolean isEmpty();
}
