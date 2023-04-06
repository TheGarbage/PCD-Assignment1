package Data;

import Monitors.CounterMonitor;
import Monitors.FilesToReadList;
import Monitors.SizeClassificationList.MultiSizeClassificationList;
import Monitors.SizeClassificationList.SingleSizeClassificationList;
import Monitors.SizeClassificationList.SizeClassificationListMonitor;
import Monitors.StateMonitor;
import Utilities.StateEnum;
import Utilities.ThreadConstants;

import java.util.ArrayList;
import java.util.Collections;

public class DataMaster {
    // Final Monitor
    final StateMonitor state = new StateMonitor();
    final FilesToReadList filesToReadList = new FilesToReadList(state);
    final StateMonitor listHasChanged = new StateMonitor();
    final StateMonitor countersHasChanged = new StateMonitor();

    // Variable Monitor
    SizeClassificationListMonitor sizeClassificationList;
    CounterMonitor[] counterList;

    // Parameters
    int n;
    int maxl;
    int ni;
    String d;

    public void initialializzation(String d, int n, int maxl, int ni){
        if(n == 1)
            this.sizeClassificationList = new SingleSizeClassificationList();
        else
            this.sizeClassificationList = new MultiSizeClassificationList(n);
        this.counterList = new CounterMonitor[ni];
        for(int i = 0; i < ni; i++)
            counterList[i] = new CounterMonitor();
        this.d = d;
        this.n = n;
        this.maxl = maxl;
        this.ni = ni;
        this.state.changeState(StateEnum.START);
    }

    public String creaStringCounters(){
        String text = "";
        if(ni < maxl) {
            for (int i = 0; i < ni; i++)
                if (i != (ni - 1) && i != (ni - 2))
                    text += "range: " + (maxl / ni * i) + "-" + (maxl / ni * (i + 1) - 1) + " = " + counterList[i].read() + "\n";
                else if (i == (ni - 2))
                    text += "range: " + (maxl / ni * i) + "-" + (maxl - 1) + " = " + counterList[i].read() + "\n";
                else
                    text += "range: " + maxl + "-... = " + counterList[i].read() + "\n";
        }
        else {
            for (int i = 0; i < ni; i++)
                if (i == maxl)
                    text += "n righe maggiori di " + maxl +  ": = " + counterList[i].read() + "\n";
                else
                    text += "n righe pari a: " + i + " = " + counterList[i].read() + "\n";
        }

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
                    item.subSequence(58 - (int) item.charAt(ThreadConstants.MAX_DIGITS), ThreadConstants.MAX_DIGITS) +
                    " - " +
                    item.subSequence(ThreadConstants.MAX_DIGITS + 1, item.length()) +
                    "\n";
        }
        return text;
    }

    public void aggiungiFile(String item, int lines) throws InterruptedException {
        if(sizeClassificationList.put(item))
            listHasChanged.changeState(StateEnum.START);
        if (lines < this.maxl)
            this.counterList[lines / (this.maxl / (this.ni - 1))].increment();
        else
            this.counterList[this.ni - 1].increment();
        countersHasChanged.changeState(StateEnum.START);
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
