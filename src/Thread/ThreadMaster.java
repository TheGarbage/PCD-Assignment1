package Thread;

import Monitor.CounterMonitor;
import Monitor.BoundedListMonitor;
import Monitor.StateMonitor;
import Monitor.UmboundedListMonitor;

import java.io.File;

public class ThreadMaster extends AbstractThread{

    private String d;
    private final ThreadSlave[] threadArray;
    final private StateMonitor state;
    Boolean start = true;


    public ThreadMaster(StateMonitor state){
        super();
        this.threadArray = new ThreadSlave[Runtime.getRuntime().availableProcessors()];
        this.filesToReadList = new UmboundedListMonitor(state);
        this.state = state;
    }

    public void initialializzation(String d, int n, int maxl, int ni){
        this.sizeClassificationList = new BoundedListMonitor(n);
        this.counterList = new CounterMonitor[ni];
        for(int i = 0; i < ni; i++)
            counterList[i] = new CounterMonitor();
        this.d = d;
        this.n = n;
        this.maxl = maxl;
        this.ni = ni;
        this.state.changeState(StateMonitor.StateEnum.START);
    }

    @Override
    public void run(){
            try {
                while(true) {
                    if(start) {
                        for(int i = 0; i < threadArray.length; i++)
                            (threadArray[i] = new ThreadSlave(this)).start();
                        start = false;
                    }
                    if(state.readState() == StateMonitor.StateEnum.START) {
                        long time = System.currentTimeMillis();
                        File file = new File(d);
                        if (!(file.getName().endsWith(".java") || file.listFiles() != null))
                            System.out.println("Sei un coglione... inserisci un path corretto");
                        else if (maxl % ni != 0 || ni < 2)
                            System.out.println("Il numero di fascie deve essere divisibile per il numero di righe massime e maggiore di 1");
                        else {
                            for (ThreadSlave t : threadArray)
                                t.inizializza();
                            filesToReadList.put(d);
                            switch (state.readState()) {
                                case CONTINUE -> {
                                    System.out.println("Fine, tempo: " + (System.currentTimeMillis() - time));
                                    System.out.println();
                                    String prova;
                                    for (int i = n - 1; i >= 0; i--) {
                                        prova = sizeClassificationList.read(i);
                                        System.out.println(String.valueOf(n - i) + ")" +
                                                " " + prova.subSequence(maxCaracters + 1, prova.length())
                                                + " - " + prova.subSequence(
                                                58 - (int) prova.charAt(maxCaracters), maxCaracters)
                                        );
                                    }
                                    System.out.println();
                                    System.out.println();
                                    for (int i = 0; i < ni; i++)
                                        if (i != (ni - 1) && i != (ni - 2))
                                            System.out.println("range: " + (maxl / ni * i) + "-" + (maxl / ni * (i + 1) - 1) + " = " + counterList[i].read());
                                        else if (i == (ni - 2))
                                            System.out.println("range: " + (maxl / ni * i) + "-" + (maxl - 1) + " = " + counterList[i].read());
                                        else
                                            System.out.println("range: " + maxl + "-... = " + counterList[i].read());
                                    System.out.println();
                                    System.out.println();
                                }
                                case OFF -> {
                                    for (Thread t : threadArray)
                                        filesToReadList.put(commitSuicideMessage);
                                    return;
                                }
                                default -> {
                                    filesToReadList.reset();
                                    state.readState();

//                                    for(Thread t : threadArray)
//                                        filesToReadList.put(commitSuicideMessage);
//                                    for(Thread t : threadArray)
//                                        t.join();
//                                    filesToReadList.reset();
//                                    start = true;
                                }
                            }
                        }
                    }
                    else{
                        for (Thread t : threadArray)
                            filesToReadList.put(commitSuicideMessage);
                        return;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}
