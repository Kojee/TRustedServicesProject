package views;

import javax.swing.*;

public class ProviderView extends JPanel {
    private JLabel testLabel;

    public ProviderView(){
        testLabel = new JLabel("Providers go here");
        add(testLabel);
    }
}
