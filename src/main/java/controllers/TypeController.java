package controllers;

import models.Country;
import models.CountryFilter;
import models.ServiceType;
import models.TypeFilter;
import utils.Observer;
import utils.Subject;
import views.CountryView;
import views.TypeView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TypeController extends Observer {
    private TypeView view;
    private TypeFilter model;

    public TypeController(TypeView view, TypeFilter model){
        this.view = view;
        this.model = model;
        //Inizializziamo la JTable delle selectable entities
        JTable SelectableEntitiesTable = view.getSelectableEntitiesTable();
        DefaultTableModel SelectableEntitiesTableModel = (DefaultTableModel)SelectableEntitiesTable.getModel();
        //Inizialmente popolo la JTable con tutte le entities
        Object[] types = model.getSelectableEntities().stream().map(x -> x.getName()).toArray();
        for (Object type: types) {
            SelectableEntitiesTableModel.addRow(new Object[]{type});
        }
        model.attach(this);
        //inizializzo vuota la JTable delle selectedEntities
        JTable SelectedEntitiesTable = view.getSelectedEntitiesTable();
        SelectableEntitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting() && SelectableEntitiesTable.getSelectedRow() != -1) {
                    model.SelectEntity(SelectableEntitiesTable.getValueAt(SelectableEntitiesTable.getSelectedRow(), 0).toString());
                    //System.out.println(SelectableEntitiesTable.getValueAt(SelectableEntitiesTable.getSelectedRow(), 0).toString());
                }
            }
        });
        SelectedEntitiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting() && SelectedEntitiesTable.getSelectedRow() != -1) {
                    model.DeselectEntity(SelectedEntitiesTable.getValueAt(SelectedEntitiesTable.getSelectedRow(), 0).toString());
                    //System.out.println(SelectedEntitiesTable.getValueAt(SelectedEntitiesTable.getSelectedRow(), 0).toString());
                }
            }
        });
    }

    @Override
    public void updateSelected(Subject s) {
        //La selected list ha subito aggiornamenti, recupero il contenuto
        List<ServiceType> types = model.getSelectedEntities();
        //aggiorno la JTable
        JTable table = view.getSelectedEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
        for (ServiceType type: types) {
            tableModel.addRow(new Object[]{type.getName()});
        }
    }

    @Override
    public void updateSelectable(Subject s) {
        //La selectable list ha subito aggiornamenti, recupero il contenuto
        List<ServiceType> types = model.getSelectableEntities();
        //aggiorno la JTable
        JTable table = view.getSelectableEntitiesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
        for (ServiceType type: types) {
            //System.out.println(country.GetCountryName());
            tableModel.addRow(new Object[]{type.getName()});
        }
    }
}