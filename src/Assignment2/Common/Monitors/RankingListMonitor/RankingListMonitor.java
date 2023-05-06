package Assignment2.Common.Monitors.RankingListMonitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

public class RankingListMonitor implements RankingMonitor {
    final ArrayList<String> rankingList = new ArrayList<>();
    final int maxSize;
    final ReentrantLock lock = new ReentrantLock();

    public RankingListMonitor(int maxSize){
        this.maxSize = maxSize;
    }

    public boolean put(String item){
        try {
            lock.lock();
            boolean added = false;
            if (rankingList.size() == maxSize) {
                if (item.compareTo(rankingList.get(0)) > 0) {
                    for (int i = 1; i < this.maxSize; i++)
                        if (item.compareTo(rankingList.get(i)) <= 0) {
                            this.rankingList.remove(0);
                            this.rankingList.add(i - 1, item);
                            added = true;
                            break;
                        } else if (i == this.maxSize - 1) {
                            this.rankingList.remove(0);
                            this.rankingList.add(i, item);
                            added = true;
                        }
                }
            } else if (this.rankingList.size() == this.maxSize - 1) {
                this.rankingList.add(item);
                Collections.sort(this.rankingList);
                added = true;
            } else {
                this.rankingList.add(item);
                added = true;
            }
            return added;
        } finally {
            lock.unlock();
        }
    }

    public ArrayList<String> read() throws InterruptedException {
        try {
            lock.lock();
            return (ArrayList<String>) this.rankingList.clone();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        try {
            lock.lock();
            return rankingList.isEmpty();
        } finally {
            lock.unlock();
        }
    }
}
