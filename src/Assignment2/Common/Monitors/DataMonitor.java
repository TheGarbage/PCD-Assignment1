package Assignment2.Common.Monitors;

import Assignment2.Common.Interface.Stoppable;
import Assignment2.Common.Utilities.StateEnum;
import Assignment2.Common.Utilities.ThreadConstants;
import Assignment2.Common.Monitors.RankingListMonitor.RankingListMonitor;
import Assignment2.Common.Monitors.RankingListMonitor.RankingMonitor;
import Assignment2.Common.Monitors.RankingListMonitor.RankingStringMonitor;

import java.util.ArrayList;
import java.util.Collections;

public class DataMonitor {
    // Final Monitor
    final StateMonitor rankingListObserverState = new StateMonitor();
    final StateMonitor countersObserverState = new StateMonitor();
    final StateMonitor processObserverState = new StateMonitor();

    // Variable Monitor
    RankingMonitor rankingMonitor;
    CounterMonitor[] countersArray;

    // Parameters
    int n;
    int maxl;
    int ni;
    String d;

    // Final message
    String finalMessage;

    //Stoppable
    Stoppable stoppable;

    public DataMonitor(String d, int n, int maxl, int ni, Stoppable stoppable){
        if (n == 1)
            this.rankingMonitor = new RankingStringMonitor();
        else
            this.rankingMonitor = new RankingListMonitor(n);
        this.countersArray = new CounterMonitor[ni];
        for (int i = 0; i < ni; i++)
            countersArray[i] = new CounterMonitor();
        this.d = d;
        this.n = n;
        this.maxl = maxl;
        this.ni = ni;
        this.stoppable = stoppable;
    }

    public String makeStringCounters(){
        int i;
        ArrayList<Integer> countersList = new ArrayList<>();
        for (i = 0; i < ni; i++)
            countersList.add(countersArray[i].read());
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

    public String makeStringList() throws InterruptedException {
        ArrayList<String> list = rankingMonitor.read();
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

    public void addFile(String item, int lines){
        if(rankingMonitor.put(item))
            rankingListObserverState.changeState(StateEnum.START);
        if (lines < this.maxl)
            this.countersArray[lines / (this.maxl / (this.ni - 1))].increment();
        else
            this.countersArray[this.ni - 1].increment();
        countersObserverState.changeState(StateEnum.START);
    }

    // Constant getter
    public StateMonitor getRankingListObserverState() {
        return rankingListObserverState;
    }

    public StateMonitor getCountersObserverState() {
        return countersObserverState;
    }
    public StateMonitor getProcessObserverState() {
        return processObserverState;
    }

    // Variable getter
    public String getD() {
        return d;
    }

    public boolean sizeClassificationListIsEmpty() throws InterruptedException {
        return rankingMonitor.isEmpty();
    }

    public void stop(){
        stoppable.shoutdown();
    }

    //Final message
    public String getFinalMessage() {
        return finalMessage;
    }

    public void setFinalMessage(String finalMessage) {
        this.finalMessage = finalMessage;
        processObserverState.changeState(StateEnum.START);
    }
}
