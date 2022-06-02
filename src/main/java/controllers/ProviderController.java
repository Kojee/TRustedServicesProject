package controllers;

import models.ProviderFilter;
import models.ServiceProvider;
import utils.Observer;
import utils.Subject;
import views.ProviderView;

import java.util.List;

public class ProviderController extends Observer {
    private ProviderView view;
    private ProviderFilter model;

    public ProviderController(ProviderView view, ProviderFilter model){
        this.view = view;
        this.model = model;
        //TODO: popolare table view
        model.attach(this);
    }

    @Override
    public void updateSelected(Subject s) {
        List<ServiceProvider> selectedProviders = model.getSelectedEntities();
        //TODO: aggiornare lista selected
    }

    @Override
    public void updateSelectable(Subject s) {
        List<ServiceProvider> selectableProviders = model.getSelectableEntities();
        //TODO: aggiornare lista selectable

    }
}
