package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProviderView extends JPanel {
    private JPanel subPanel;
    private JTable selectableEntitiesTable;
    private JTable selectedEntitiesTable;
    public ProviderView(){
    //Creo il pannello che conterrà le due tabelle
        subPanel = new JPanel();
        subPanel.setPreferredSize(new Dimension(500, 420));
        add(subPanel);
    //Creo la tabella dei Provider selezionabili
        selectableEntitiesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{ "Providers selezionabili"}));
        //Imposto la dimensione della tabella
        selectableEntitiesTable.setPreferredScrollableViewportSize(new Dimension(480,180));
        //Imposto lo sfondo della tabella bianco
        selectableEntitiesTable.setFillsViewportHeight(true);
        //Rimuovo l'editor di default per non permettere la modifica dei dati
        selectableEntitiesTable.setDefaultEditor(Object.class, null);
        selectableEntitiesTable.setRowSelectionAllowed(false);
        //Aggiungo un pannello per lo scorrimento delle righe
        JScrollPane selectableEntitiesScroll = new JScrollPane(selectableEntitiesTable);
        selectableEntitiesScroll.setVisible(true);
        subPanel.add(selectableEntitiesScroll);
    //Creo la tabella dei Provider selezionati
        selectedEntitiesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{ "Providers selezionati"}));
        //Imposto la dimensione della tabella
        selectedEntitiesTable.setPreferredScrollableViewportSize(new Dimension(480,180));
        //Imposto lo sfondo della tabella bianco
        selectedEntitiesTable.setFillsViewportHeight(true);
        //Rimuovo l'editor di default per non permettere la modifica dei dati
        selectableEntitiesTable.setDefaultEditor(Object.class, null);
        selectedEntitiesTable.setRowSelectionAllowed(false);
        //Aggiungo un pannello per lo scorrimento delle righe
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