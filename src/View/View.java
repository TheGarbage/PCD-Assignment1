package View;

import Data.DataMaster;
import Monitors.StateMonitor;
import Utilities.StateEnum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class View extends JFrame implements ActionListener, WindowListener, ChangeListener{
    final String rankingTitle = "-------------------   RANKING -------------------";
    final String intervalsTitle = "----------   INTERVALS DIVISION   ----------";
    final int maxSizePath = 45;

    JSpinner n = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    JSpinner maxl = new JSpinner(new SpinnerNumberModel(2, 2, Integer.MAX_VALUE, 1));
    JTextArea state = new JTextArea();
    JTextArea directory = new JTextArea(" No directory selected");
    JComboBox<Integer> ni = new JComboBox<Integer>();
    JButton selezionaDirecotory = new JButton("   Select Direcotory   ");
    JTextArea list = new JTextArea(rankingTitle, 1, 22);
    JScrollPane listScroll = new JScrollPane(list);
    JTextArea counters = new JTextArea(intervalsTitle, 1, 22);
    JScrollPane countersScroll = new JScrollPane(counters);
    JButton start = new JButton("   Start   ");
    JButton stop = new JButton("   Stop   ");
    JFileChooser fileChooser = new JFileChooser("/");
    final DataMaster dataMaster;
    final StateMonitor stateMain;

    public View(DataMaster dataMaster) {
        super("PCD-Assignment1");
        setSize(600, 600);
        JPanel panelParameter = new JPanel();
        panelParameter.add(new JLabel("N: "));
        panelParameter.add(n);
        panelParameter.add(new JLabel("          MAXL: "));
        panelParameter.add(maxl);
        panelParameter.add(new JLabel("          NI: "));
        ni.addItem(3);
        ni.setPrototypeDisplayValue(1111111);
        panelParameter.add(ni);
        ((JSpinner.DefaultEditor) n.getEditor()).getTextField().setColumns(5);
        ((JSpinner.DefaultEditor) maxl.getEditor()).getTextField().setColumns(5);
        panelParameter.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelSelector = new JPanel();
        directory.setEditable(false);
        directory.setColumns(30);
        panelSelector.add(directory);
        panelSelector.add(selezionaDirecotory);
        panelSelector.setBorder(new EmptyBorder(10, 10, 15, 10));

        JPanel panelNord = new JPanel();
        panelNord.setLayout(new BorderLayout());
        panelNord.add(BorderLayout.NORTH, panelSelector);
        panelNord.add(BorderLayout.SOUTH, panelParameter);

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());
        list.setEditable(false);
        list.setBorder(new EmptyBorder(10, 10, 10, 10));
        counters.setEditable(false);
        counters.setBorder(new EmptyBorder(10, 10, 10, 10));
        countersScroll.setBorder(BorderFactory.createLineBorder(Color.black));
        countersScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
        listScroll.setBorder(BorderFactory.createLineBorder(Color.black));
        listScroll.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelCenter.add(BorderLayout.WEST, listScroll);
        panelCenter.add(BorderLayout.EAST, countersScroll);

        JPanel infoPanel = new JPanel();
        state.setText(" Idle");
        state.setEditable(false);
        state.setColumns(15);
        infoPanel.add(new JLabel("State: "));
        infoPanel.add(state);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel bottonPanel = new JPanel();
        bottonPanel.setLayout(new BorderLayout());
        start.setEnabled(false);
        stop.setEnabled(false);
        bottonPanel.add(BorderLayout.WEST, start);
        bottonPanel.add(BorderLayout.EAST, stop);
        bottonPanel.setBorder(new EmptyBorder(10, 380, 10, 10));

        JPanel panelSouth = new JPanel();
        panelSouth.setLayout(new BorderLayout());
        panelSouth.add(BorderLayout.NORTH, infoPanel);
        panelSouth.add(BorderLayout.SOUTH, bottonPanel);


        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());
        panelMain.add(BorderLayout.NORTH, panelNord);
        panelMain.add(BorderLayout.CENTER, panelCenter);
        panelMain.add(BorderLayout.SOUTH, panelSouth);
        panelMain.setBorder(new EmptyBorder(10, 10, 10, 10));

        setContentPane(panelMain);

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        addWindowListener(this);
        selezionaDirecotory.addActionListener(this);
        maxl.addChangeListener(this);
        start.addActionListener(this);
        stop.addActionListener(this);
        this.dataMaster = dataMaster;
        this.stateMain = dataMaster.getState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selezionaDirecotory) {
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (path.length() > maxSizePath)
                    directory.setText(" ..." + path.substring(path.length() - maxSizePath));
                else
                    directory.setText(path);
                start.setEnabled(true);
            }
        }
        else if(e.getSource() == start){
            start(false);
            dataMaster.initialializzation(directory.getText(), (Integer)n.getValue(), (int) maxl.getValue(), (int) ni.getSelectedItem());
            state.setText(" Processing...");
        }
        else if(e.getSource() == stop){
            start(true);
            stateMain.changeState(StateEnum.START);
            state.setText(" Stopped");
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
        stateMain.changeState(StateEnum.OFF);
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
            if(maxlValue == 2)
                ni.addItem(3);
            else {
                for (int i = 1; i < maxlValue; i++)
                    if (maxlValue % i == 0)
                        ni.addItem(i + 1);
                if (maxlValue < Integer.MAX_VALUE)
                    ni.addItem(maxlValue + 1);
            }
            SwingUtilities.updateComponentTreeUI(this);
        }
    }

    public void setFinish(String text){
        SwingUtilities.invokeLater(() -> {
            start(true);
            state.setText(text);
        });
    }

    public void setListText(String text){
        SwingUtilities.invokeLater(() -> list.setText(rankingTitle + "\n\n" + text));
    }

    public void setCountersText(String text){
        SwingUtilities.invokeLater(() -> counters.setText(intervalsTitle + "\n\n" + text));
    }

    public void open(){
        javax.swing.SwingUtilities.invokeLater(() -> this.setVisible(true));
    }
}
