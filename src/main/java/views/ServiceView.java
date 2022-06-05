package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServiceView extends JPanel{

    private JTable servicesTable;
    private JButton servicesButton;
    public ServiceView() {
    //Creo la tabella dei servizi
        servicesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{"Servizio", "ID Nazione", "Tipi", "Stato", "ID Provider"}));
        //Imposto la dimensione della tabella
        servicesTable.setPreferredScrollableViewportSize(new Dimension(1400, 280));
        //Imposto lo sfondo della tabella bianco
        servicesTable.setFillsViewportHeight(true);
        //Rimuovo l'editor di default per non permettere la modifica dei dati
        servicesTable.setDefaultEditor(Object.class, null);
        servicesTable.setRowSelectionAllowed(false);
        //Imposto la dimensione delle colonne
        servicesTable.getColumnModel().getColumn(0).setPreferredWidth(500);
        servicesTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        servicesTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        servicesTable.getColumnModel().getColumn(3).setPreferredWidth(400);
        servicesTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        //Aggiungo un pannello per lo scorrimento delle righe
        JScrollPane serviceScroll = new JScrollPane(servicesTable);
        serviceScroll.setVisible(true);
        add(serviceScroll);
    //Creo il pulsante per filtrare i servizi
        servicesButton = new JButton("Filtra");
        servicesButton.setVisible(true);
        add(servicesButton);
    }
    public JTable getServicesTable() {
        return servicesTable;
    }
    public JButton getServicesButton() { return servicesButton; }
}
