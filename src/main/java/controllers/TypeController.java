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
    }
    @Override
    public void updateSelected(Subject s) {
        List<ServiceType> selectedTypes = model.getSelectedEntities();
        //TODO: aggiornare table selected
    }

    @Override
    public void updateSelectable(Subject s) {
        List<ServiceType> selectableTypes = model.getSelectableEntities();
        //TODO: aggiornare table selectable
    }
}
