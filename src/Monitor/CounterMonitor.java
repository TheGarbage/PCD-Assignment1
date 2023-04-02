package Monitor;

public class CounterMonitor {

    long count = 0;

    public synchronized void increment(){
        count++;
    }

    public synchronized long decrement(){
        return --count;
    }

    public synchronized long read(){ //Saranno letti da solo un thread
        return count;
    }

    public synchronized void reset(){
        count = 0;
    }
}
