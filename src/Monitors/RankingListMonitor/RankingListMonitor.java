package Monitors.RankingListMonitor;
import java.util.ArrayList;
import java.util.Collections;

public class RankingListMonitor implements RankingMonitor {
    final ArrayList<String> rankingList = new ArrayList<>();
    final int maxSize;

    public RankingListMonitor(int maxSize){
        this.maxSize = maxSize;
    }

    public synchronized boolean put(String item){
        boolean added = false;
        if(rankingList.size() == maxSize) {
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
        }
        else if(this.rankingList.size() == this.maxSize - 1){
            this.rankingList.add(item);
            Collections.sort(this.rankingList);
            added = true;
        }
        else{
            this.rankingList.add(item);
            notify();
            added = true;
        }
        return added;
    }

    public synchronized ArrayList<String> read() throws InterruptedException {
        while (rankingList.isEmpty()) wait();
        return (ArrayList<String>)this.rankingList.clone();
    }

    @Override
    public boolean isEmpty() {
        return rankingList.isEmpty();
    }
}
