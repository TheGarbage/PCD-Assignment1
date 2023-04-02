import Monitor.StateMonitor;
import Thread.ThreadMaster;

public class MainMultiThread {

    public static void main(String[] args) throws InterruptedException {
        StateMonitor state = new StateMonitor();
        ThreadMaster threadMaster = new ThreadMaster(state);
        threadMaster.initialializzation("G:\\Università\\Non_Date\\Programmazione concorrente\\Prova 1\\TestFolder2", 10, 50, 5);
        Thread.sleep(2000);
        threadMaster.initialializzation("G:\\Università\\Non_Date\\Programmazione concorrente\\Prova 1\\TestFolder2", 5, 50, 10);
        Thread.sleep(2000);
        threadMaster.initialializzation("G:\\Università\\Non_Date\\Programmazione concorrente\\Prova 1\\TestFolder2", 5, 50, 10);
        Thread.sleep(100);
        state.changeState(StateMonitor.StateEnum.START);
        Thread.sleep(1000);
        threadMaster.initialializzation("G:\\Università\\Non_Date\\Programmazione concorrente\\Prova 1\\TestFolder2", 2, 20, 2);
        Thread.sleep(2000);
        state.changeState(StateMonitor.StateEnum.OFF);
    }
}
