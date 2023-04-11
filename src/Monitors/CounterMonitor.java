package Monitors;

public class CounterMonitor {

    int count = 0;

    public synchronized void increment(){
        count++;
    }

    public synchronized int read(){ //Saranno letti da solo un thread
        return count;
    }
}
