package Monitors.SizeClassificationListMonitors;
import java.util.ArrayList;
import java.util.Collections;

public class MultiSizeClassificationListMonitor implements SizeClassificationListMonitor {
    final ArrayList<String> MultiSizeClassificationListMonitor = new ArrayList<>();
    final int maxSize;

    public MultiSizeClassificationListMonitor(int maxSize){
        this.maxSize = maxSize;
    }

    public synchronized boolean put(String item){ // Sistemare
        boolean added = false;
        if(MultiSizeClassificationListMonitor.size() == maxSize) {
            if (item.compareTo(MultiSizeClassificationListMonitor.get(0)) > 0) {
                for (int i = 1; i < this.maxSize; i++)
                    if (item.compareTo(MultiSizeClassificationListMonitor.get(i)) <= 0) {
                        this.MultiSizeClassificationListMonitor.remove(0);
                        this.MultiSizeClassificationListMonitor.add(i - 1, item);
                        added = true;
                        break;
                    } else if (i == this.maxSize - 1) {
                        this.MultiSizeClassificationListMonitor.remove(0);
                        this.MultiSizeClassificationListMonitor.add(i, item);
                        added = true;
                    }
            }

        }
        else if(this.MultiSizeClassificationListMonitor.size() == this.maxSize - 1){
            this.MultiSizeClassificationListMonitor.add(item);
            Collections.sort(this.MultiSizeClassificationListMonitor);
            added = true;
        }
        else{
            this.MultiSizeClassificationListMonitor.add(item);
            notify();
            added = true;
        }
        return added;
    }

    public synchronized ArrayList<String> read() throws InterruptedException {
        while (MultiSizeClassificationListMonitor.isEmpty()) wait();
        return (ArrayList<String>)this.MultiSizeClassificationListMonitor.clone();
    }

    @Override
    public boolean isEmpty() {
        return MultiSizeClassificationListMonitor.isEmpty();
    }
}
