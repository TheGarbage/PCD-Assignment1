package Monitors.SizeClassificationList;

import Utilities.ThreadConstants;

import java.util.ArrayList;

public class SingleSizeClassificationList implements SizeClassificationListMonitor{
    String maxSizeFile = ThreadConstants.STRING_PREFIX;

    @Override
    public synchronized boolean put(String item) {
        if (item.compareTo(maxSizeFile) > 0) {
            maxSizeFile = item;
            return true;
        }
        return false;
    }

    @Override
    public synchronized ArrayList<String> read(){
        ArrayList<String> list = new ArrayList<String>(); // Non c'è bisogno di attesa perchè si attiva al primo cambio
        list.add(maxSizeFile);
        return list;
    }

    @Override
    public boolean isEmpty() {
        return maxSizeFile.equals(ThreadConstants.STRING_PREFIX);
    }


}
