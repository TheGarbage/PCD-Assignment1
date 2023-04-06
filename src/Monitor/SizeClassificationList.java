package Monitor;
import java.util.ArrayList;
import java.util.Collections;

public class SizeClassificationList {
    final ArrayList<String> list = new ArrayList<>();
    final int maxSize;

    public SizeClassificationList(int maxSize){
        this.maxSize = maxSize;
    }

    public synchronized boolean put(String item) throws InterruptedException { // Sistemare
        boolean added = false;
        if(list.size() == maxSize) {
            if (item.compareTo(list.get(0)) > 0) {
                if (maxSize != 1) {
                    for (int i = 1; i < this.maxSize; i++)
                        if (item.compareTo(list.get(i)) <= 0) {
                            this.list.remove(0);
                            this.list.add(i - 1, item);
                            added = true;
                            break;
                        } else if (i == this.maxSize - 1) {
                            this.list.remove(0);
                            this.list.add(i, item);
                            added = true;
                        }
                }
                else {
                    this.list.remove(0);
                    this.list.add(0, item);
                    added = true;
                }
            }

        }
        else if(this.list.size() == this.maxSize - 1){
            this.list.add(item);
            Collections.sort(this.list);
            added = true;
        }
        else{
            this.list.add(item);
            notify();
            added = true;
        }
        return added;
    }

    public synchronized ArrayList<String> read() throws InterruptedException {
        while (list.isEmpty()) wait();
        return (ArrayList<String>)this.list.clone();
    }
}
