import View.View;

public class MainView {

    public static void main(String[] args) throws InterruptedException {
        javax.swing.SwingUtilities.invokeLater(() -> {
            (new View()).setVisible(true);
        });
    }
}
