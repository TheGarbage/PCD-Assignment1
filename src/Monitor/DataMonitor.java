package Monitor;

import java.util.ArrayList;
import java.util.Collections;

public class DataMonitor {
    // Final Monitor
    final StateMonitor state = new StateMonitor();
    final FilesToReadList filesToReadList = new FilesToReadList(state);
    final StateMonitor listHasChanged = new StateMonitor();
    final StateMonitor countersHasChanged = new StateMonitor();

    // Variable Monitor
    SizeClassificationList sizeClassificationList;
    CounterMonitor[] counterList;

    // Parameters
    int n;
    int maxl;
    int ni;
    String d;

    // Thread Utilities
    final static public int maxCaracters = 10;
    final static public String stringPrefix = "0000000000";
    final static public String terminationMessage = "Terminate";
    final static public String skipMessagge = "Skip";

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

    public String creaStringCounters(){
        String text = "";
        for (int i = 0; i < ni; i++)
            if (i != (ni - 1) && i != (ni - 2))
                text += "range: " + (maxl / ni * i) + "-" + (maxl / ni * (i + 1) - 1) + " = " + counterList[i].read() + "\n";
            else if (i == (ni - 2))
                text += "range: " + (maxl / ni * i) + "-" + (maxl - 1) + " = " + counterList[i].read() + "\n";
            else
                text += "range: " + maxl + "-... = " + counterList[i].read() + "\n";
        return text;
    }

    public String creaStringList() throws InterruptedException {
        String item, text = "";
        ArrayList<String> list = sizeClassificationList.read();
        if(list.size() < n && list.size() != 1)
            Collections.sort(list);
        for (int i = list.size() - 1; i >= 0; i--) { // Deve adattarsi alla dimensione della lista
            item = list.get(i);
            text += (n - i) + ")" +
                    " " +
                    item.subSequence(58 - (int) item.charAt(maxCaracters), maxCaracters) +
                    " - " +
                    item.subSequence(maxCaracters + 1, item.length()) +
                    "\n";
        }
        return text;
    }

    public void aggiungiFile(String item, int lines) throws InterruptedException {
        if(sizeClassificationList.put(item))
            listHasChanged.changeState(StateMonitor.StateEnum.START);
        if (lines < this.maxl)
            this.counterList[lines / (this.maxl / (this.ni - 1))].increment();
        else
            this.counterList[this.ni - 1].increment();
        countersHasChanged.changeState(StateMonitor.StateEnum.START);
    }

    public StateMonitor getState() {
        return state;
    }

    public FilesToReadList getFilesToReadList() {
        return filesToReadList;
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
}
