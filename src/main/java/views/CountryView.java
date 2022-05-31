package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CountryView extends JPanel {
    //TODO: aggiungere JScrollPane per rendere scrollabile la lista
    //TODO: aggiungere seconda JTable per lista selectedEntities

    private JTable selectableEntitiesTable;
    public CountryView(){
        selectableEntitiesTable = new JTable(new DefaultTableModel(new Object[]{ "Nome"}, 0));
        add(selectableEntitiesTable);
    }

    public JTable getSelectableEntitiesTable() {
        return selectableEntitiesTable;
    }
}
