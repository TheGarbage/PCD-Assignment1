package Assignment1.Monitors.RankingListMonitor;

import Assignment1.Utilities.ThreadConstants;

import java.util.ArrayList;

public class RankingStringMonitor implements RankingMonitor {
    String rankingString = ThreadConstants.STRING_PREFIX;

    @Override
    public synchronized boolean put(String item) {
        if (item.compareTo(rankingString) > 0) {
            rankingString = item;
            return true;
        }
        return false;
    }

    @Override
    public synchronized ArrayList<String> read(){
        ArrayList<String> list = new ArrayList<String>(); // Non c'è bisogno di attesa perchè si attiva al primo cambio
        list.add(rankingString);
        return list;
    }

    @Override
    public boolean isEmpty() {
        return rankingString.equals(ThreadConstants.STRING_PREFIX);
    }


}
