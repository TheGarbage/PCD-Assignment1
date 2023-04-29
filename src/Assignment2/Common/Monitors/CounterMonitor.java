package Assignment2.Common.Monitors;

import java.util.concurrent.locks.ReentrantLock;

public class CounterMonitor {
    int count = 0;
    final ReentrantLock lock = new ReentrantLock();

    public void increment(){
        try {
            lock.lock();
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int read(){ //Saranno letti da solo un thread
        try {
            lock.lock();
            return count;
        } finally {
            lock.unlock();
        }
    }
}
