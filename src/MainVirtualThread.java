import Assignment2.Common.View.View;
import Assignment2.VirtualThreads.VirtualThreadSourceAnalyser;

public class MainVirtualThread {
    static String d = "/home/thegarbage/Scaricati";
    static int n = 10;
    static int maxl = 100;
    static int ni = 6;

    public static void main(String[] args) throws InterruptedException {
        new VirtualThreadSourceAnalyser().getReportpublic(d, n, maxl, ni);
        new View(new VirtualThreadSourceAnalyser());
    }
}
