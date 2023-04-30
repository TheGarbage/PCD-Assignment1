package Assignment2.Common.Interface;

import Assignment2.Common.Monitors.DataMonitor;

public interface SourceAnalyser {
    void getReportpublic(String d, int n, int maxl, int ni) throws InterruptedException;
    DataWrapper analyzeSources(String d, int n, int maxl, int ni);
}
