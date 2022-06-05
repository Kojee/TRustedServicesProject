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
        //Inizializzo vuota la JTable dei servizi
        JTable ServicesTable = view.getServicesTable();
        //Inizializzo il JButton
        JButton ServicesButton = view.getServicesButton();
        ActionListener actionListener = new ActionListener() {
            //Al click del pulsante aggiorno i servizi
            public void actionPerformed(ActionEvent event) {
                updateService();
            }
        };
        ServicesButton.setActionCommand("Filter");
        ServicesButton.addActionListener(actionListener);
    }

    private void updateService() {
        //La lista dei servizi ha subito aggiornamenti, recupero il contenuto
        List<Service> services = model.getServices();
        //Aggiorno la JTable
        JTable table = view.getServicesTable();
        DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
        //Svuoto tutti gli elementi dalla JTable
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
        //Inserisco tutti gli elementi aggiornati nella JTable
        for (Service service: services) {
            //I tipi di servizi possono essere più di uno, rimuovo le quadre agli estremi e li inserisco uno
            // dopo l'altro separati da una virgola
            String serviceTypes = service.getqServiceTypes().toString();
            tableModel.addRow(new Object[]{service.getServiceName(), service.getCountryCode(), serviceTypes.substring(1, serviceTypes.length()-1), service.getCurrentStatus(), service.getTspId()});
        }
    }
}
