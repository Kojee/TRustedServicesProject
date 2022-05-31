package controllers;

import models.CountryFilter;
import views.CountryView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class CountryController {
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
        //TODO: inizializzare vuota la JTable delle selectedEntities

        //TODO: aggiungere sottoscrizione eventi di selezione delle righe delle JTable in CountryView
        //      la gestione di tali eventi richiamerà i metodi Select e Deselect nel model
        //TODO: aggiungere sottoscrizione eventi emessi da CountryFilter (sono ancora da implementare)
        //      la gestione di tali eventi andrà ad aggiungere/rimuovere le righe dalle JTable
    }
}
