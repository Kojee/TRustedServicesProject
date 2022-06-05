package models;

import utils.Subject;
import views.StatusView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StatusFilter  extends Subject {
    private ITrustedServiceApi serviceApi;
    private List<ServiceStatus> selectableEntities;
    private List<ServiceStatus> selectedEntities;

    public StatusFilter(ITrustedServiceApi serviceApi) {
        this.serviceApi = serviceApi;
        this.selectableEntities = serviceApi.GetServiceStatuses(null);
        this.selectedEntities = new ArrayList<>();
    }

    public void SelectEntity(String statusName){
        Optional<ServiceStatus> c = selectableEntities.stream().filter(x -> x.getStatus().equals(statusName)).findFirst();
        if(c.isPresent()){
            selectableEntities.remove(c.get());
            selectedEntities.add(c.get());
            setSelectable();
            setSelected();
        }
    }

    public void DeselectEntity(String statusName){
        Optional<ServiceStatus> c = selectedEntities.stream().filter(x -> x.getStatus().equals(statusName)).findFirst();
        if(c.isPresent()){
            selectableEntities.add(c.get());
            selectedEntities.remove(c.get());
            setSelectable();
            setSelected();
        }
    }

    public void FilterSelectableEntities(Filter filter) {
        /*Creo un filtro dove non popolo gli status,
          altrimenti filtrerei anche per gli status attualmente selezionati.
          Questo non va bene perchè così facendo gli status selezionabili
          sarebbero sempre al più uguali agli status selezionati.
         */
        if(filter == null)
            throw new IllegalArgumentException();
        Filter selectableFilter = new Filter();
        selectableFilter.setCountries(filter.getCountries());
        selectableFilter.setProviders(filter.getProviders());
        selectableFilter.setTypes(filter.getTypes());
        List<ServiceStatus> newSelectableEntities = serviceApi.GetServiceStatuses(selectableFilter);
        //Rimuoviamo quelli già selezionati
        selectableEntities = newSelectableEntities
                .stream()
                .filter(c -> !selectedEntities.contains(c))
                .collect(Collectors.toList());
        setSelectable();
    }

    public List<ServiceStatus> getSelectableEntities(){
        return selectableEntities;
    }

    public List<ServiceStatus> getSelectedEntities(){
        return selectedEntities;
    }
}
