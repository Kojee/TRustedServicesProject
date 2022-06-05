package controllers;

import models.Country;
import models.Service;
import models.ServiceFilter;
import utils.Subject;
import views.ServiceView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ServiceController {
    private ServiceView view;
    private ServiceFilter model;

    public ServiceController(ServiceView view, ServiceFilter model){
        this.view = view;
        this.model = model;
        //Inizializziamo la JTable delle selectable entities
        JTable ServicesTable = view.getServicesTable();
        DefaultTableModel ServicesTableModel = (DefaultTableModel)ServicesTable.getModel();
        JButton ServicesButton = view.getServicesButton();
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                updateService();
            }
        };
        ServicesButton.setActionCommand("Filter");
        ServicesButton.addActionListener(actionListener);
    }

    private void updateService() {
        //La selected list ha subito aggiornamenti, recupero il contenuto
        List<Service> services = model.getServices();
        //aggiorno la JTable
        JTable table = view.getServicesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        //svuoto tutti gli elementi dalla JTable
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
        //inserisco tutti gli elementi aggiornati nella JTable
        for (Service service: services) {
            tableModel.addRow(new Object[]{service.getServiceName()});
        }
    }
}
