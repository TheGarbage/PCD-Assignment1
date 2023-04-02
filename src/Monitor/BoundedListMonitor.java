package Monitor;
import java.util.ArrayList;
import java.util.Collections;

public class BoundedListMonitor {
    final ArrayList<String> list = new ArrayList<>();
    int nReader = 0;
    final int maxSize;

    public BoundedListMonitor(int maxSize){
        this.maxSize = maxSize;
    }

    public synchronized void put(String item) throws InterruptedException {
        if(list.size() == maxSize)
            for (int i = 0; i < this.maxSize; i++) {
                if ((item.compareTo(list.get(i)) <= 0 && i != 0) || i == this.maxSize - 1) {
                    this.list.remove(0);
                    this.list.add(i, item);
                    break;
                }
            }
        else if(this.list.size() == this.maxSize - 1){
            this.list.add(item);
            Collections.sort(this.list);
        }
        else{
            this.list.add(item);
        }
    }

    public String read(int i) throws InterruptedException { //Potrebbe scatenare un'eccezione se è vuoto
        synchronized (this){
            nReader++;
        }
        String item = this.list.get(i);
        synchronized (this){
            nReader--;
            if(nReader == 0)
                this.notify();
        }
        return item;
    }

}
