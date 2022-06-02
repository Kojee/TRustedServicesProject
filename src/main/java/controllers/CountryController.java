package controllers;

import models.Country;
import models.CountryFilter;
import utils.Observer;
import utils.Subject;
import views.CountryView;

import javax.swing.*;
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
        JTable table = view.getSelectableEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        //Inizialmente popolo la JTable con tutte le entities
        tableModel.addRow(model.getSelectableEntities().stream().map(x -> x.GetCountryName()).toArray());
        model.attach(this);
        //TODO: inizializzare vuota la JTable delle selectedEntities
        //TODO: aggiungere sottoscrizione eventi di selezione delle righe delle JTable in CountryView
        //      la gestione di tali eventi richiamerà i metodi Select e Deselect nel model
    }

    @Override
    public void updateSelected(Subject s) {
        List<Country> selectedCountries = model.getSelectedEntities();
        //TODO: aggiornare la JTable
    }

    @Override
    public void updateSelectable(Subject s) {
        List<Country> selectableCountries = model.getSelectableEntities();
        //TODO: aggiornare la JTable
    }
}
