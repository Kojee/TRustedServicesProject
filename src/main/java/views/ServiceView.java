package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServiceView extends JPanel{

    private JTable servicesTable;
    private JButton servicesButton;
    public ServiceView() {
        servicesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{"Servizi"}));
        servicesTable.setPreferredScrollableViewportSize(new Dimension(500, 180));
        servicesTable.setFillsViewportHeight(true);
        servicesTable.setDefaultEditor(Object.class, null);
        servicesTable.setRowSelectionAllowed(false);
        JScrollPane serviceScroll = new JScrollPane(servicesTable);
        serviceScroll.setVisible(true);
        add(serviceScroll);
        servicesButton = new JButton("Filtra");
        servicesButton.setVisible(true);
        add(servicesButton);
    }
    public JTable getServicesTable() {
        return servicesTable;
    }
    public JButton getServicesButton() {
        return servicesButton;
    }
}
