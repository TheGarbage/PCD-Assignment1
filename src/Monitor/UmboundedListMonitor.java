package Monitor;
import java.util.ArrayList;

public class UmboundedListMonitor {
    final ArrayList<String> list = new ArrayList<>();
    CounterMonitor counter = new CounterMonitor();
    final StateMonitor stateMainThread;

    public UmboundedListMonitor(StateMonitor stateMainThread){
        this.stateMainThread = stateMainThread;
    }

    public synchronized void put(String item) throws InterruptedException {
            this.list.add(item);
            this.notify();
    }

    public synchronized String get() throws InterruptedException {
        while (list.isEmpty()) wait();
        counter.increment();
        return this.list.remove(0);
    }

    public synchronized boolean isEmpty(){
        return this.list.isEmpty();
    }

    public void decrementActiveMonitorCounter(){
        if(counter.decrement() == 0 && isEmpty())
            stateMainThread.changeState(StateMonitor.StateEnum.CONTINUE);
    }

    public synchronized void reset(){
        list.clear();
        counter.reset();
    }

}
