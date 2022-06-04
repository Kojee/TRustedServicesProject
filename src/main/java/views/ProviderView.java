package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProviderView extends JPanel {
    private JPanel subPanel;
    private JTable selectableEntitiesTable;
    private JTable selectedEntitiesTable;

    public ProviderView(){
        subPanel = new JPanel();
        subPanel.setPreferredSize(new Dimension(250, 500));
        add(subPanel);

        selectableEntitiesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{ "Selectable Provider"}));
        selectableEntitiesTable.setPreferredScrollableViewportSize(new Dimension(200,200));
        selectableEntitiesTable.setFillsViewportHeight(true);
        JScrollPane selectableEntitiesScroll = new JScrollPane(selectableEntitiesTable);
        selectableEntitiesScroll.setVisible(true);
        subPanel.add(selectableEntitiesScroll);

        selectedEntitiesTable = new JTable(new DefaultTableModel(new Object[][]{}, new Object[]{ "Selected Provider"}));
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
}
