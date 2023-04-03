package Monitor;
import java.util.ArrayList;
import java.util.Collections;

public class SizeClassificationList {
    final ArrayList<String> list = new ArrayList<>();
    final int maxSize;

    public SizeClassificationList(int maxSize){
        this.maxSize = maxSize;
    }

    public synchronized void put(String item) throws InterruptedException {
        if(list.size() == maxSize) {
            if (item.compareTo(list.get(0)) > 0)
                for (int i = 1; i < this.maxSize; i++)
                    if (item.compareTo(list.get(i)) <= 0) {
                        this.list.remove(0);
                        this.list.add(i - 1, item);
                        break;
                    }
                    else if(i == this.maxSize - 1){
                        this.list.remove(0);
                        this.list.add(i, item);
                    }
        }
        else if(this.list.size() == this.maxSize - 1){
            this.list.add(item);
            Collections.sort(this.list);
        }
        else{
            this.list.add(item);
            notify();
        }
    }

    public synchronized String read(int i) throws InterruptedException {
        while (list.isEmpty()) wait();
        return this.list.get(i);
    }
}
