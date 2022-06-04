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
        subPanel.setPreferredSize(new Dimension(250, 500));
        add(subPanel);
        //Creo tabella Paesi selezionabili
        selectableEntitiesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{ "Selectable Country"}));
        selectableEntitiesTable.setPreferredScrollableViewportSize(new Dimension(200,200));
        selectableEntitiesTable.setFillsViewportHeight(true);
        JScrollPane selectableEntitiesScroll = new JScrollPane(selectableEntitiesTable);
        selectableEntitiesScroll.setVisible(true);
        subPanel.add(selectableEntitiesScroll);
        //Creo tabella Paesi selezionati
        selectedEntitiesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{ "Selected Country"}));
        selectedEntitiesTable.setPreferredScrollableViewportSize(new Dimension(200,200));
        selectedEntitiesTable.setFillsViewportHeight(true);
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

    private void RowClicked(java.awt.event.MouseEvent evt) {
        JTable source = (JTable)evt.getSource();
        int row = source.rowAtPoint( evt.getPoint() );
        int column = source.columnAtPoint( evt.getPoint() );
        String s=source.getModel().getValueAt(row, column)+"";

    }
}
