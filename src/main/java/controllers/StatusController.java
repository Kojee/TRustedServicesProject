package controllers;

import models.Country;
import models.CountryFilter;
import models.ServiceStatus;
import models.StatusFilter;
import utils.Observer;
import utils.Subject;
import views.CountryView;
import views.StatusView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StatusController extends Observer {
    private StatusView view;
    private StatusFilter model;

    public StatusController(StatusView view, StatusFilter model){
        this.view = view;
        this.model = model;
        //Inizializziamo la JTable delle selectable entities
        JTable SelectableEntitiesTable = view.getSelectableEntitiesTable();
        DefaultTableModel SelectableEntitiesTableModel = (DefaultTableModel)SelectableEntitiesTable.getModel();
        //Inizialmente popolo la JTable con tutte le entities
        Object[] statuses = model.getSelectableEntities().stream().map(x -> x.getStatus()).toArray();
        for (Object status: statuses) {
            SelectableEntitiesTableModel.addRow(new Object[]{status});
        }
        model.attach(this);
        //inizializzo vuota la JTable delle selectedEntities
        JTable SelectedEntitiesTable = view.getSelectedEntitiesTable();
        SelectableEntitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                //if per verificare che il click sia avvenuto e non eseguire l'evento due volte
                if(!event.getValueIsAdjusting() && SelectableEntitiesTable.getSelectedRow() != -1) {
                    //seleziono la cella che è stata cliccata
                    model.SelectEntity(SelectableEntitiesTable.getValueAt(SelectableEntitiesTable.getSelectedRow(), 0).toString());
                }
            }
        });
        SelectedEntitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                //if per verificare che il click sia avvenuto e non eseguire l'evento due volte
                if(!event.getValueIsAdjusting() && SelectedEntitiesTable.getSelectedRow() != -1) {
                    //deseleziono la cella che è stata cliccata
                    model.DeselectEntity(SelectedEntitiesTable.getValueAt(SelectedEntitiesTable.getSelectedRow(), 0).toString());
                }
            }
        });
    }

    @Override
    public void updateSelected(Subject s) {
        //La selected list ha subito aggiornamenti, recupero il contenuto
        List<ServiceStatus> statuses = model.getSelectedEntities();
        //aggiorno la JTable
        JTable table = view.getSelectedEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        //svuoto tutti gli elementi dalla JTable
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
        //inserisco tutti gli elementi aggiornati nella JTable
        for (ServiceStatus status: statuses) {
            tableModel.addRow(new Object[]{status.getStatus()});
        }
    }

    @Override
    public void updateSelectable(Subject s) {
        //La selectable list ha subito aggiornamenti, recupero il contenuto
        List<ServiceStatus> statuses = model.getSelectableEntities();
        //aggiorno la JTable
        JTable table = view.getSelectableEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        //svuoto tutti gli elementi dalla JTable
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
        //inserisco tutti gli elementi aggiornati nella JTable
        for (ServiceStatus status: statuses) {
            tableModel.addRow(new Object[]{status.getStatus()});
        }
    }
}