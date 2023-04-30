package Assignment2.Common.View;

import Assignment2.Common.Interface.DataWrapper;
import Assignment2.Common.Interface.SourceAnalyser;
import Assignment2.Common.Observers.CountersObeserver;
import Assignment2.Common.Observers.ProcessObservers;
import Assignment2.Common.Observers.RankingListObserver;
import Assignment2.Common.Utilities.StateEnum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public class View extends JFrame implements ActionListener, WindowListener, ChangeListener, PopupMenuListener {
    // Text utilities
    final String rankingTitle = "-------------------   RANKING -------------------";
    final String intervalsTitle = "----------   INTERVALS DIVISION   ----------";
    final int maxSizePath = 45;

    JSpinner n = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
    JSpinner maxl = new JSpinner(new SpinnerNumberModel(2, 2, Integer.MAX_VALUE, 1));
    JComboBox<Integer> ni = new JComboBox<>();
    JTextArea processState = new JTextArea();
    JTextArea directorySelected = new JTextArea(" No directory selected");
    JButton directoryButton = new JButton("   Select Direcotory   ");
    JTextArea rankingText = new JTextArea(rankingTitle, 1, 22);
    JScrollPane rankingScrollPane = new JScrollPane(rankingText);
    JTextArea intervalsText = new JTextArea(intervalsTitle, 1, 22);
    JScrollPane intervalsScrollPane = new JScrollPane(intervalsText);
    JButton startButton = new JButton("   Start   ");
    JButton stopButton = new JButton("   Stop   ");
    JFileChooser fileChooser = new JFileChooser("/");

    // Constant
    final SourceAnalyser sourceAnalyser;
    DataWrapper dataWrapper;
    ProcessObservers processObservers;
    CountersObeserver countersObeserver;
    RankingListObserver rankingListObserver;

    public View(SourceAnalyser sourceAnalyser) {
        super("PCD-Assignment1");
        setSize(600, 600);
        JPanel parametersPanel = new JPanel();
        parametersPanel.add(new JLabel("N: "));
        parametersPanel.add(n);
        parametersPanel.add(new JLabel("          MAXL: "));
        parametersPanel.add(maxl);
        parametersPanel.add(new JLabel("          NI: "));
        ni.addItem(3);
        ni.setPrototypeDisplayValue(1111111);
        parametersPanel.add(ni);
        ((JSpinner.DefaultEditor) n.getEditor()).getTextField().setColumns(5);
        ((JSpinner.DefaultEditor) maxl.getEditor()).getTextField().setColumns(5);
        parametersPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel selectorPanel = new JPanel();
        directorySelected.setEditable(false);
        directorySelected.setColumns(30);
        selectorPanel.add(directorySelected);
        selectorPanel.add(directoryButton);
        selectorPanel.setBorder(new EmptyBorder(10, 10, 15, 10));

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(BorderLayout.NORTH, selectorPanel);
        northPanel.add(BorderLayout.SOUTH, parametersPanel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        rankingText.setEditable(false);
        rankingText.setBorder(new EmptyBorder(10, 10, 10, 10));
        intervalsText.setEditable(false);
        intervalsText.setBorder(new EmptyBorder(10, 10, 10, 10));
        intervalsScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        intervalsScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        rankingScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        rankingScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(BorderLayout.WEST, rankingScrollPane);
        centerPanel.add(BorderLayout.EAST, intervalsScrollPane);

        JPanel infoPanel = new JPanel();
        processState.setText(" Parameter entry");
        processState.setEditable(false);
        processState.setColumns(20);
        infoPanel.add(new JLabel("State: "));
        infoPanel.add(processState);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        buttonPanel.add(BorderLayout.WEST, startButton);
        buttonPanel.add(BorderLayout.EAST, stopButton);
        buttonPanel.setBorder(new EmptyBorder(10, 380, 10, 10));

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(BorderLayout.NORTH, infoPanel);
        southPanel.add(BorderLayout.SOUTH, buttonPanel);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(BorderLayout.NORTH, northPanel);
        mainPanel.add(BorderLayout.CENTER, centerPanel);
        mainPanel.add(BorderLayout.SOUTH, southPanel);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        setContentPane(mainPanel);

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        addWindowListener(this);
        directoryButton.addActionListener(this);
        maxl.addChangeListener(this);
        ni.addPopupMenuListener(this);
        startButton.addActionListener(this);
        stopButton.addActionListener(this);

        this.sourceAnalyser = sourceAnalyser;
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == directoryButton) {
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (path.length() > maxSizePath)
                    directorySelected.setText(" ..." + path.substring(path.length() - maxSizePath));
                else
                    directorySelected.setText(path);
                startButton.setEnabled(true);
                processState.setText(" Waiting to start");
            }
        }
        else if(e.getSource() == startButton){
            setIdle(false);
            this.dataWrapper = sourceAnalyser.analyzeSources(fileChooser.getSelectedFile().getAbsolutePath(), (int)n.getValue(), (int) maxl.getValue(), (int) ni.getSelectedItem());
            this.processObservers = new ProcessObservers(this, this.dataWrapper);
            this.countersObeserver = new CountersObeserver(this, this.dataWrapper);
            this.rankingListObserver = new RankingListObserver(this, this.dataWrapper);
            processState.setText(" Processing...");
        }
        else if(e.getSource() == stopButton){
            stopSourceAnalyser();
            processState.setText(" Stopped");
            setIdle(true);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if(dataWrapper != null)
            stopSourceAnalyser();
        dispose();
        System.exit(0);
    }

    private void stopSourceAnalyser(){
        dataWrapper.stop();
        dataWrapper.getProcessObserverState().changeState(StateEnum.STOP);
        dataWrapper.getCountersObserverState().changeState(StateEnum.STOP);
        dataWrapper.getRankingListObserverState().changeState(StateEnum.STOP);
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == maxl) {
            int niValue = (int) ni.getSelectedItem();
            int maxlValue = (int) maxl.getValue();
            if(maxlValue < (niValue - 1) || maxlValue % (niValue - 1) != 0) {
                ni.removeAllItems();
                if (maxlValue == 2)
                    ni.addItem(3);
                else
                    ni.addItem(2);
            }
        }
    }

    private void setIdle(boolean enable){
        n.setEnabled(enable);
        ni.setEnabled(enable);
        maxl.setEnabled(enable);
        directoryButton.setEnabled(enable);
        startButton.setEnabled(enable);
        stopButton.setEnabled(!enable);
    }

    public void setFinish(String text){
        SwingUtilities.invokeLater(() -> {
            setIdle(true);
            processState.setText(text);
        });
    }


    public void setRankingText(String text) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> rankingText.setText(rankingTitle + "\n\n" + text));
    }

    public void setIntervalsText(String text) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> intervalsText.setText(intervalsTitle + "\n\n" + text));
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if (e.getSource() == ni) {
            try {
                maxl.commitEdit();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
            int niValue = (int) ni.getSelectedItem();
            ni.removeAllItems();
            int maxlValue = (int) maxl.getValue();
            if(maxlValue != 2) {
                for (int i = 1; i < maxlValue; i++)
                    if (maxlValue % i == 0)
                        ni.addItem(i + 1);
                if (maxlValue < Integer.MAX_VALUE)
                    ni.addItem(maxlValue + 1);
                ni.setSelectedItem(niValue);
            }
            else
                ni.addItem(3);
        }
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {}
}
