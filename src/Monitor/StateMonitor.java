package Monitor;

public class StateMonitor {
    public enum StateEnum{OFF, WAIT, START, CONTINUE};

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
