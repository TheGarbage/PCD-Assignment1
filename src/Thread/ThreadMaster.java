package Thread;

import Monitor.CounterMonitor;
import Monitor.SizeClassificationList;
import Monitor.StateMonitor;
import Monitor.FilesToReadList;
import View.View;

import java.io.File;

public class ThreadMaster extends AbstractThread{

    private String d;
    private final ThreadSlave[] threadArray;
    final private StateMonitor state = new StateMonitor();
    View myView;


    public ThreadMaster(){
        super();
        this.threadArray = new ThreadSlave[Runtime.getRuntime().availableProcessors()];
        this.filesToReadList = new FilesToReadList(state);
        for(int i = 0; i < threadArray.length; i++)
            threadArray[i] = new ThreadSlave(this);
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
                this.myView = new View(this, state);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    this.myView.setVisible(true);
                });
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
                            StateMonitor.StateEnum stateEnum= state.readState();
                            if(stateEnum == StateMonitor.StateEnum.CONTINUE)
                                stampa(time);
                            else {
                                filesToReadList.reset();
                                state.readState();
                                filesToReadList.activate();
                                if(stateEnum == StateMonitor.StateEnum.OFF)
                                    break;
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
        String item, text = "";
        for (int i = n - 1; i >= 0; i--) { // Deve adattarsi alla dimensione della lista
            item = sizeClassificationList.read(i);
            text += (n - i) + ")" +
                    " " + item.subSequence(maxCaracters + 1, item.length())
                    + " - " + item.subSequence(
                    58 - (int) item.charAt(maxCaracters), maxCaracters) + "\n";
        }
        myView.setListText(text);
        text = "";
        for (int i = 0; i < ni; i++)
            if (i != (ni - 1) && i != (ni - 2))
                text += "range: " + (maxl / ni * i) + "-" + (maxl / ni * (i + 1) - 1) + " = " + counterList[i].read() + "\n";
            else if (i == (ni - 2))
                text += "range: " + (maxl / ni * i) + "-" + (maxl - 1) + " = " + counterList[i].read() + "\n";
            else
                text += "range: " + maxl + "-... = " + counterList[i].read() + "\n";
        myView.setCountersText(text);
        myView.setFinish("Fine, tempo: " + (System.currentTimeMillis() - time));
    }
}