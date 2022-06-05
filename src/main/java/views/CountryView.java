package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CountryView extends JPanel {
    private JPanel subPanel;
    private JTable selectableEntitiesTable;
    private JTable selectedEntitiesTable;
    public CountryView(){
        //Creo il pannello che conterrà le due tabelle
        subPanel = new JPanel();
        subPanel.setPreferredSize(new Dimension(200, 420));
        add(subPanel);
        //Creo tabella Paesi selezionabili
        selectableEntitiesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{ "Nazioni selezionabili"}));
        selectableEntitiesTable.setPreferredScrollableViewportSize(new Dimension(180,180));
        selectableEntitiesTable.setFillsViewportHeight(true);
        selectableEntitiesTable.setDefaultEditor(Object.class, null);
        selectableEntitiesTable.setRowSelectionAllowed(false);
        JScrollPane selectableEntitiesScroll = new JScrollPane(selectableEntitiesTable);
        selectableEntitiesScroll.setVisible(true);
        subPanel.add(selectableEntitiesScroll);
        //Creo tabella Paesi selezionati
        selectedEntitiesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{ "Nazioni selezionate"}));
        selectedEntitiesTable.setPreferredScrollableViewportSize(new Dimension(180,180));
        selectedEntitiesTable.setFillsViewportHeight(true);
        selectableEntitiesTable.setDefaultEditor(Object.class, null);
        selectedEntitiesTable.setRowSelectionAllowed(false);
        JScrollPane selectedEntitiesScroll = new JScrollPane(selectedEntitiesTable);
        selectedEntitiesScroll.setVisible(true);
        subPanel.add(selectedEntitiesScroll);
    }

    public JTable getSelectableEntitiesTable() {
        return selectableEntitiesTable;
    }

    public JTable getSelectedEntitiesTable() {
        return selectedEntitiesTable;
    }
}
