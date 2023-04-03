package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class View extends JFrame{
    JSpinner n = new JSpinner();
    JSpinner maxl = new JSpinner();
    JTextField state = new JTextField( 20);
    JComboBox<Integer> ni = new JComboBox<Integer>();
    JButton selezionaDirecotory = new JButton("Seleziona Direcotory");
    JTextArea list = new JTextArea(" Classification: ", 3, 45);
    JScrollPane listScroll = new JScrollPane(list);
    JTextArea counters = new JTextArea(" Counters: ", 3, 45);
    JScrollPane countersScroll = new JScrollPane(counters);

    public View(int w, int h){
        super("PCD-Assignment1");
        setSize(1200, 800);
        JPanel panelParameter = new JPanel();
        panelParameter.add(new JLabel("n: "));
        panelParameter.add(n);
        panelParameter.add(new JLabel("    maxl: "));
        panelParameter.add(maxl);
        panelParameter.add(new JLabel("    ni: "));
        panelParameter.add(ni);
        //ni.setEnabled(false);
        ni.addItem(2);
        ni.addItem(3);
        ni.addItem(4);
        ((JSpinner.DefaultEditor)n.getEditor()).getTextField().setColumns(5);
        ((JSpinner.DefaultEditor)maxl.getEditor()).getTextField().setColumns(5);
        panelParameter.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel panelEmpty = new JPanel();

        JPanel panelSelector = new JPanel();
        panelSelector.add(selezionaDirecotory);
        panelSelector.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel panelNord = new JPanel();
        panelNord.setLayout(new BorderLayout());
        panelNord.add(BorderLayout.WEST, panelParameter);
        panelNord.add(BorderLayout.CENTER, panelEmpty);
        panelNord.add(BorderLayout.EAST, panelSelector);


        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());
        list.setEditable(false);
        counters.setEditable(false);
        countersScroll.setBorder(BorderFactory.createLineBorder(Color.black));
        countersScroll.setBorder(new EmptyBorder(10, 20, 10, 20));
        listScroll.setBorder(BorderFactory.createLineBorder(Color.black));
        listScroll.setBorder(new EmptyBorder(10, 20, 10, 20));
        panelCenter.add(BorderLayout.WEST, listScroll);
        panelCenter.add(BorderLayout.EAST, countersScroll);

        JPanel infoPanel = new JPanel();
        state.setText("Idle");
        state.setEditable(false);
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
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Controllare per fare uscite pulite
    }
}
