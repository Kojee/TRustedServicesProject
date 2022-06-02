package controllers;

import models.ServiceFilter;
import views.ServiceView;

public class ServiceController {
    private ServiceView view;
    private ServiceFilter model;

    public ServiceController(ServiceView view, ServiceFilter model){
        this.view = view;
        this.model = model;
    }

    //TODO: implementare funzione per gestire il click del pulsante che richiede i servizi
    //Andrà poi a richiamare model.getServices() e aggiornerà la UI
}
