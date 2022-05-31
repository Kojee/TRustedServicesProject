package views;

import javax.swing.*;

public class StatusView  extends JPanel {
    private JLabel testLabel;

    public StatusView(){
        testLabel = new JLabel("Statuses go here");
        add(testLabel);
    }
}
