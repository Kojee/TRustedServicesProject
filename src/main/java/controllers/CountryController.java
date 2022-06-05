package controllers;

import models.Country;
import models.CountryFilter;
import utils.Observer;
import utils.Subject;
import views.CountryView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class CountryController extends Observer {
    private CountryView view;
    private CountryFilter model;

    public CountryController(CountryView view, CountryFilter model){
        this.view = view;
        this.model = model;
        //Inizializzo la JTable delle selectable entities
        JTable SelectableEntitiesTable = view.getSelectableEntitiesTable();
        DefaultTableModel SelectableEntitiesTableModel = (DefaultTableModel)SelectableEntitiesTable.getModel();
        //Inizialmente popolo la JTable con tutte le entities
        Object[] countries = model.getSelectableEntities().stream().map(x -> x.GetCountryName()).toArray();
        for (Object country: countries) {
            SelectableEntitiesTableModel.addRow(new Object[]{country});
        }
        model.attach(this);
        //Inizializzo vuota la JTable delle selectedEntities
        JTable SelectedEntitiesTable = view.getSelectedEntitiesTable();
        SelectableEntitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                //if per verificare che il click sia avvenuto e non eseguire l'evento due volte
                if(!event.getValueIsAdjusting() && SelectableEntitiesTable.getSelectedRow() != -1) {
                    //Seleziono la cella che è stata cliccata
                    model.SelectEntity(SelectableEntitiesTable.getValueAt(SelectableEntitiesTable.getSelectedRow(), 0).toString());
                }
            }
        });
        SelectedEntitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                //if per verificare che il click sia avvenuto e non eseguire l'evento due volte
                if(!event.getValueIsAdjusting() && SelectedEntitiesTable.getSelectedRow() != -1) {
                    //Deseleziono la cella che è stata cliccata
                    model.DeselectEntity(SelectedEntitiesTable.getValueAt(SelectedEntitiesTable.getSelectedRow(), 0).toString());
                }
            }
        });
    }

    @Override
    public void updateSelected(Subject s) {
        //La selected list ha subito aggiornamenti, recupero il contenuto
        List<Country> countries = model.getSelectedEntities();
        //Aggiorno la JTable
        JTable table = view.getSelectedEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        //Svuoto tutti gli elementi dalla JTable
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
        //Inserisco tutti gli elementi aggiornati nella JTable
        for (Country country: countries) {
            tableModel.addRow(new Object[]{country.GetCountryName()});
        }
    }

    @Override
    public void updateSelectable(Subject s) {
        //La selectable list ha subito aggiornamenti, recupero il contenuto
        List<Country> countries = model.getSelectableEntities();
        //Aggiorno la JTable
        JTable table = view.getSelectableEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        //Svuoto tutti gli elementi dalla JTable
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
        //Inserisco tutti gli elementi aggiornati nella JTable
        for (Country country: countries) {
            tableModel.addRow(new Object[]{country.GetCountryName()});
        }
    }
}
