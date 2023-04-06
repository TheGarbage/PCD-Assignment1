package Monitors;

import Utilities.StateEnum;

public class StateMonitor {
    private StateEnum stateEnum = StateEnum.WAIT;

    public synchronized void changeState(StateEnum stateEnum){
        this.stateEnum = stateEnum;
        notify();
    }

    public synchronized StateEnum readState() throws InterruptedException {
        while(this.stateEnum == StateEnum.WAIT) wait();
        StateEnum state = stateEnum;
        stateEnum = StateEnum.WAIT;
        return state;
    }
}
