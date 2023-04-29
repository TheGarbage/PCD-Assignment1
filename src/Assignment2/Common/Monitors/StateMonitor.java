package Assignment2.Common.Monitors;

import Assignment2.Common.Utilities.StateEnum;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class StateMonitor {
    private StateEnum stateEnum = StateEnum.WAIT;
    final ReentrantLock lock = new ReentrantLock();
    final Condition waitCodition = lock.newCondition();

    public void changeState(StateEnum stateEnum){
        try {
            lock.lock();
            this.stateEnum = stateEnum;
            waitCodition.notify();
        } finally {
            lock.unlock();
        }
    }

    public StateEnum readState() throws InterruptedException {
        try {
            lock.lock();
            while (this.stateEnum == StateEnum.WAIT) waitCodition.await();
            StateEnum state = stateEnum;
            stateEnum = StateEnum.WAIT;
            return state;
        } finally {
            lock.unlock();
        }
    }
}
