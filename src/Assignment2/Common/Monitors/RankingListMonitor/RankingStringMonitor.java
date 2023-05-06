package Assignment2.Common.Monitors.RankingListMonitor;

import Assignment2.Common.Utilities.ThreadConstants;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class RankingStringMonitor implements RankingMonitor {
    String rankingString = ThreadConstants.STRING_PREFIX;
    final ReentrantLock lock = new ReentrantLock();

    @Override
    public boolean put(String item) {
        try {
            lock.lock();
            if (item.compareTo(rankingString) > 0) {
                rankingString = item;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized ArrayList<String> read(){
        try {
            lock.lock();
            ArrayList<String> list = new ArrayList<String>();
            list.add(rankingString);
            return list;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        try{
            lock.lock();
            return rankingString.equals(ThreadConstants.STRING_PREFIX);
        } finally {
            lock.unlock();
        }
    }


}
