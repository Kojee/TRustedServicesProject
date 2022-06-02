package controllers;

import models.ServiceType;
import models.TypeFilter;
import utils.Observer;
import utils.Subject;
import views.TypeView;

import java.util.List;

public class TypeController extends Observer {
    private TypeView view;
    private TypeFilter model;

    public TypeController(TypeView view, TypeFilter model){
        this.view = view;
        this.model = model;
        //TODO: popolare table view
        model.attach(this);
        //TODO: aggiungere sottoscrizione eventi di selezione delle righe delle JTable in CountryView
        //      la gestione di tali eventi richiamerà i metodi Select e Deselect nel model
    }
    @Override
    public void updateSelected(Subject s) {
        //La selected list ha subito aggiornamenti, recupero il contenuto
        List<ServiceType> selectedTypes = model.getSelectedEntities();
        //TODO: aggiornare table selected
    }

    @Override
    public void updateSelectable(Subject s) {
        //La selectable list ha subito aggiornamenti, recupero il contenuto
        List<ServiceType> selectableTypes = model.getSelectableEntities();
        //TODO: aggiornare table selectable
    }
}
