package Thread;

import Monitor.CounterMonitor;
import Monitor.SizeClassificationList;
import Monitor.StateMonitor;
import Monitor.FilesToReadList;

import java.io.File;

public class ThreadMaster extends AbstractThread{

    private String d;
    private final ThreadSlave[] threadArray;
    final private StateMonitor state;


    public ThreadMaster(StateMonitor state){
        super();
        this.threadArray = new ThreadSlave[Runtime.getRuntime().availableProcessors()];
        this.filesToReadList = new FilesToReadList(state);
        for(int i = 0; i < threadArray.length; i++)
            (threadArray[i] = new ThreadSlave(this)).start();
        this.state = state;
        this.start();
    }

    public void initialializzation(String d, int n, int maxl, int ni){
        this.sizeClassificationList = new SizeClassificationList(n);
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
                                case CONTINUE -> stampa(time);
                                case OFF -> {
                                    filesToReadList.reset();
                                    state.readState();
                                    for (Thread t : threadArray)
                                        filesToReadList.put(commitSuicideMessage);
                                    return;
                                }
                                default -> {
                                    filesToReadList.reset();
                                    state.readState();
                                    filesToReadList.activate();
                                }
                            }
                        }
                    }
                    else
                        break;
                }
                for (Thread t : threadArray)
                    filesToReadList.put(commitSuicideMessage);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }

    public void stampa(long time) throws InterruptedException {
        System.out.println("Fine, tempo: " + (System.currentTimeMillis() - time));
        System.out.println();
        String prova;
        for (int i = n - 1; i >= 0; i--) {
            prova = sizeClassificationList.read(i);
            System.out.println((n - i) + ")" +
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
}
