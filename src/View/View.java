package View;

import Monitor.DataMonitor;
import Monitor.StateMonitor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import Thread.ThreadMaster;

public class View extends JFrame implements ActionListener, WindowListener, ChangeListener{
    JSpinner n = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    JSpinner maxl = new JSpinner(new SpinnerNumberModel(2, 2, Integer.MAX_VALUE, 1));
    JTextArea state = new JTextArea();
    JTextArea directory = new JTextArea("No directory selected");
    JScrollPane directoryScroll = new JScrollPane(directory);
    JComboBox<Integer> ni = new JComboBox<Integer>();
    JButton selezionaDirecotory = new JButton("Seleziona Direcotory");
    JTextArea list = new JTextArea(" Classification: ", 3, 45);
    JScrollPane listScroll = new JScrollPane(list);
    JTextArea counters = new JTextArea(" Counters: ", 3, 45);
    JScrollPane countersScroll = new JScrollPane(counters);
    JButton start = new JButton("Start");
    JButton stop = new JButton("Stop");
    JFileChooser fileChooser = new JFileChooser("/");
    final DataMonitor dataMonitor;
    final StateMonitor stateMain;

    public View(DataMonitor dataMonitor) {
        super("PCD-Assignment1");
        setSize(1200, 800);
        JPanel panelParameter = new JPanel();
        panelParameter.add(new JLabel("n: "));
        panelParameter.add(n);
        panelParameter.add(new JLabel("    maxl: "));
        panelParameter.add(maxl);
        panelParameter.add(new JLabel("    ni: "));
        ni.addItem(2);
        ni.setPrototypeDisplayValue(1111111);
        panelParameter.add(ni);
        ((JSpinner.DefaultEditor) n.getEditor()).getTextField().setColumns(5);
        ((JSpinner.DefaultEditor) maxl.getEditor()).getTextField().setColumns(5);
        panelParameter.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel bottonPanel = new JPanel();
        bottonPanel.setLayout(new BorderLayout());
        start.setEnabled(false);
        stop.setEnabled(false);
        bottonPanel.add(BorderLayout.WEST, start);
        bottonPanel.add(BorderLayout.EAST, stop);
        bottonPanel.setBorder(new EmptyBorder(10, 115, 10, 80));

        JPanel panelSelector = new JPanel();
        directory.setEditable(false);
        directory.setColumns(20);
        panelSelector.add(directoryScroll);
        panelSelector.add(selezionaDirecotory);
        panelSelector.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel panelNord = new JPanel();
        panelNord.setLayout(new BorderLayout());
        panelNord.add(BorderLayout.WEST, panelParameter);
        panelNord.add(BorderLayout.CENTER, bottonPanel);
        panelNord.add(BorderLayout.EAST, panelSelector);


        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());
        list.setEditable(false);
        list.setBorder(new EmptyBorder(20, 20, 20, 20));
        counters.setEditable(false);
        counters.setBorder(new EmptyBorder(20, 20, 20, 20));
        countersScroll.setBorder(BorderFactory.createLineBorder(Color.black));
        countersScroll.setBorder(new EmptyBorder(10, 20, 10, 20));
        listScroll.setBorder(BorderFactory.createLineBorder(Color.black));
        listScroll.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelCenter.add(BorderLayout.WEST, listScroll);
        panelCenter.add(BorderLayout.EAST, countersScroll);

        JPanel infoPanel = new JPanel();
        state.setText("Idle");
        state.setEditable(false);
        state.setColumns(20);
        infoPanel.add(new JLabel("State: "));
        infoPanel.add(state);
        infoPanel.setBorder(new EmptyBorder(10, 20, 10, 20));


        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());
        panelMain.add(BorderLayout.NORTH, panelNord);
        panelMain.add(BorderLayout.CENTER, panelCenter);
        panelMain.add(BorderLayout.SOUTH, infoPanel);
        panelMain.setBorder(new EmptyBorder(10, 20, 10, 20));

        setContentPane(panelMain);

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        addWindowListener(this);
        selezionaDirecotory.addActionListener(this);
        maxl.addChangeListener(this);
        start.addActionListener(this);
        stop.addActionListener(this);
        this.dataMonitor = dataMonitor;
        this.stateMain = dataMonitor.getState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selezionaDirecotory) {
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                directory.setText(fileChooser.getSelectedFile().getAbsolutePath());
                if (directory.getText().length() > 35)
                    SwingUtilities.updateComponentTreeUI(this);
                start.setEnabled(true);
            }
        }
        else if(e.getSource() == start){
            start(false);
            dataMonitor.initialializzation(directory.getText(), (Integer)n.getValue(), (int) maxl.getValue(), (int) ni.getSelectedItem());
            state.setText("Processing...");
        }
        else if(e.getSource() == stop){
            start(true);
            stateMain.changeState(StateMonitor.StateEnum.START);
            state.setText("Stopped");
        }
    }

    private void start(boolean enable){
        n.setEnabled(enable);
        ni.setEnabled(enable);
        maxl.setEnabled(enable);
        selezionaDirecotory.setEnabled(enable);
        start.setEnabled(enable);
        stop.setEnabled(!enable);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        stateMain.changeState(StateMonitor.StateEnum.OFF);
        dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == maxl) {
            ni.removeAllItems();
            int maxlValue = (int) maxl.getValue();
            for (int i = 2; i < maxlValue; i++)
                if (maxlValue % i == 0)
                    ni.addItem(i);
            ni.addItem(maxlValue);
        }
    }

    public void setFinish(String text){
        SwingUtilities.invokeLater(() -> {
            start(true);
            state.setText(text);
        });
    }

    public void setListText(String text){
        SwingUtilities.invokeLater(() -> {
            list.setText(text);
        });
    }

    public void setCountersText(String text){
        SwingUtilities.invokeLater(() -> {
            counters.setText(text);
        });
    }

    public void open(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }
}
