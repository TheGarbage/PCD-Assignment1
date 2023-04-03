import Monitor.StateMonitor;
import View.View;

public class MainView {

    public static void main(String[] args) throws InterruptedException {
        javax.swing.SwingUtilities.invokeLater(() -> {
            (new View(1200, 800)).setVisible(true);
        });
    }
}
