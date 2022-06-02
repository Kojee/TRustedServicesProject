package controllers;

import models.ServiceStatus;
import models.StatusFilter;
import utils.Observer;
import utils.Subject;
import views.StatusView;

import java.util.List;

public class StatusController extends Observer {
    private StatusView view;
    private StatusFilter model;
    public StatusController(StatusView view, StatusFilter model){
        this.view = view;
        this.model = model;
        //TODO: popolare table della view
        model.attach(this);
        //TODO: aggiungere sottoscrizione eventi di selezione delle righe delle JTable in CountryView
        //      la gestione di tali eventi richiamerà i metodi Select e Deselect nel model
    }
    @Override
    public void updateSelected(Subject s) {
        //La selected list ha subito aggiornamenti, recupero il contenuto
        List<ServiceStatus> selectedStatuses = model.getSelectedEntities();
        //TODO: aggiornare table selected
    }

    @Override
    public void updateSelectable(Subject s) {
        //La selectable list ha subito aggiornamenti, recupero il contenuto
        List<ServiceStatus> selectableStatuses = model.getSelectableEntities();
        //TODO: aggiornare table selectable
    }
}
