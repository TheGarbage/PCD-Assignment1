package Monitor;
import java.util.ArrayList;

public class FilesToReadList {
    final ArrayList<String> list = new ArrayList<>();
    CounterMonitor counter = new CounterMonitor();
    final StateMonitor stateMainThread;
    private boolean active = true;

    public FilesToReadList(StateMonitor stateMainThread){
        this.stateMainThread = stateMainThread;
    }

    public synchronized void put(String item) throws InterruptedException {
        if(active) {
            this.list.add(item);
            this.notify();
        }
    }

    public synchronized String get() throws InterruptedException {
        while (list.isEmpty()) wait();
        counter.increment();
        return this.list.remove(0);
    }

    public synchronized void decrementActiveMonitorCounter(){
        if(counter.decrement() == 0 && this.list.isEmpty())
            stateMainThread.changeState(StateMonitor.StateEnum.CONTINUE);
    }

    public synchronized void reset(){
        list.clear();
        list.add(DataMonitor.skipMessagge); // Cosi ho la certezza che almeno uno attivo ci sarà
        active = false;
    }

    public synchronized void activate(){
        active = true;
    }
}
