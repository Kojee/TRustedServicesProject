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
        //Inizializziamo la JTable delle selectable entities
        JTable SelectableEntitiesTable = view.getSelectableEntitiesTable();
        DefaultTableModel SelectableEntitiesTableModel = (DefaultTableModel)SelectableEntitiesTable.getModel();
        //Inizialmente popolo la JTable con tutte le entities
        Object[] countries = model.getSelectableEntities().stream().map(x -> x.GetCountryName()).toArray();
        for (Object country: countries) {
            SelectableEntitiesTableModel.addRow(new Object[]{country});
        }
        model.attach(this);
        //inizializzo vuota la JTable delle selectedEntities
        JTable SelectedEntitiesTable = view.getSelectedEntitiesTable();
        //TODO: aggiungere sottoscrizione eventi di selezione delle righe delle JTable in CountryView
        //      la gestione di tali eventi richiamerà i metodi Select e Deselect nel model
        SelectableEntitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                model.SelectCountry(SelectableEntitiesTable.getValueAt(SelectableEntitiesTable.getSelectedRow(), 0).toString());
                System.out.println(SelectableEntitiesTable.getValueAt(SelectableEntitiesTable.getSelectedRow(), 0).toString());
            }
        });
    }

    @Override
    public void updateSelected(Subject s) {
        //La selected list ha subito aggiornamenti, recupero il contenuto
        List<Country> selectedCountries = model.getSelectedEntities();
        //aggiorno la JTable
        JTable table = view.getSelectedEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.addRow(selectedCountries.stream().map(x -> x.GetCountryName()).toArray());
    }

    @Override
    public void updateSelectable(Subject s) {
        //La selectable list ha subito aggiornamenti, recupero il contenuto
        List<Country> selectableCountries = model.getSelectableEntities();
        //aggiorno la JTable
        JTable table = view.getSelectableEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        tableModel.addRow(selectableCountries.stream().map(x -> x.GetCountryName()).toArray());
    }
}
