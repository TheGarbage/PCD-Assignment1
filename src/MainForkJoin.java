import Assignment2.Common.View.View;
import Assignment2.Executor.ForkJoin.ForkJoinSourceAnalyser;

public class MainForkJoin {
    static String d = "/home/thegarbage/Scaricati";
    static int n = 10;
    static int maxl = 100;
    static int ni = 6;

    public static void main(String[] args) throws InterruptedException {
        new ForkJoinSourceAnalyser().getReportpublic(d, n, maxl, ni);
        new View(new ForkJoinSourceAnalyser());
    }
}
