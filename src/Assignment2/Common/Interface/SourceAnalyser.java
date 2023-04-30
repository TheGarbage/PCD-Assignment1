package Assignment2.Common.Interface;

public interface SourceAnalyser {
    void getReportpublic(String d, int n, int maxl, int ni) throws InterruptedException;
    DataWrapper analyzeSources(String d, int n, int maxl, int ni);
}
