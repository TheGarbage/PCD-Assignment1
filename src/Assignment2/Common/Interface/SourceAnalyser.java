package Assignment2.Common.Interface;

import Assignment2.Common.Monitors.DataMonitor;

public interface SourceAnalyser {
    //void getReportpublic(String d, int n, int maxl, int ni);
    DataMonitor analyzeSources(String d, int n, int maxl, int ni) throws InterruptedException;
}
