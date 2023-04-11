package Monitors;

import Monitors.SizeClassificationListMonitors.MultiSizeClassificationListMonitor;
import Monitors.SizeClassificationListMonitors.SingleSizeClassificationListMonitor;
import Monitors.SizeClassificationListMonitors.SizeClassificationListMonitor;
import Utilities.StateEnum;
import Utilities.ThreadConstants;

import java.util.ArrayList;
import java.util.Collections;

public class DataMonitor {
    // Final Monitor
    final StateMonitor state = new StateMonitor();
    final PathsToReadListMonitor pathsToReadListMonitor = new PathsToReadListMonitor(state);
    final StateMonitor listHasChanged = new StateMonitor();
    final StateMonitor countersHasChanged = new StateMonitor();

    // Variable Monitor
    SizeClassificationListMonitor sizeClassificationListMonitor;
    CounterMonitor[] countersArray;

    // Parameters
    int n;
    int maxl;
    int ni;
    String d;

    // Writers-Readers
    int nReader = 0;

    public synchronized void initialializzation(String d, int n, int maxl, int ni) throws InterruptedException {
        while(nReader > 0) wait();
        if(n == 1)
            this.sizeClassificationListMonitor = new SingleSizeClassificationListMonitor();
        else
            this.sizeClassificationListMonitor = new MultiSizeClassificationListMonitor(n);
        this.countersArray = new CounterMonitor[ni];
        for(int i = 0; i < ni; i++)
            countersArray[i] = new CounterMonitor();
        this.d = d;
        this.n = n;
        this.maxl = maxl;
        this.ni = ni;
        this.state.changeState(StateEnum.START);
    }

    public String creaStringCounters(){
        int i;
        ArrayList<Integer> countersList = new ArrayList<>();
        synchronized(this){
            nReader++;
        }
        for (i = 0; i < ni; i++)
            countersList.add(countersArray[i].read());
        synchronized(this){
            nReader--;
            notify();
        }
        String text = "";
        if(ni < maxl) {
            for (i = 0; i < ni; i++) {
                text = text.concat("- Interval [ ");
                if (i != (ni - 1))
                    text =  text.concat((maxl / (ni - 1) * i) + " - " + (maxl / (ni - 1) * (i + 1) - 1));
                else
                    text = text.concat(maxl + " - inf");
                text = text.concat(" ]:   " + countersList.get(i) + "\n");
            }
        }
        else {
            for (i = 0; i < ni; i++) {
                if (i == maxl)
                    text = text.concat(" - more than" + maxl);
                else
                    text = text.concat(" - " + i);
                text = text.concat(" row(s): =   " + countersList.get(i) + "\n");
            }
        }
        return text;
    }

    public String creaStringList() throws InterruptedException {
        synchronized(this){
            nReader++;
        }
        ArrayList<String> list = sizeClassificationListMonitor.read();
        synchronized(this){
            nReader--;
            notify();
        }
        String item, text = "";
        if(list.size() < n && list.size() != 1)
            Collections.sort(list);
        for (int i = list.size() - 1; i >= 0; i--) {
            item = list.get(i);
            text = text.concat((n - i) + ")" +
                    " " +
                    item.subSequence(58 - (int) item.charAt(ThreadConstants.MAX_DIGITS), ThreadConstants.MAX_DIGITS) +
                    " - " +
                    item.subSequence(ThreadConstants.MAX_DIGITS + 1, item.length()) +
                    "\n");
        }
        return text;
    }

    // No synchronized method
    public void aggiungiFile(String item, int lines) throws InterruptedException {
        if(sizeClassificationListMonitor.put(item))
            listHasChanged.changeState(StateEnum.START);
        if (lines < this.maxl)
            this.countersArray[lines / (this.maxl / (this.ni - 1))].increment();
        else
            this.countersArray[this.ni - 1].increment();
        countersHasChanged.changeState(StateEnum.START);
    }

    // Constant getter
    public StateMonitor getState() {
        return state;
    }

    public PathsToReadListMonitor getFilesToReadList() {
        return pathsToReadListMonitor;
    }

    public StateMonitor getListHasChanged() {
        return listHasChanged;
    }

    public StateMonitor getCountersHasChanged() {
        return countersHasChanged;
    }

    // Variable getter
    public String getD() {
        return d;
    }

    public boolean sizeClassificationListIsEmpty() throws InterruptedException {
        return sizeClassificationListMonitor.isEmpty();
    }
}
