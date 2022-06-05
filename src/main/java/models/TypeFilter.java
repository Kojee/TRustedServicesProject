package models;

import utils.Subject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TypeFilter  extends Subject {
    private ITrustedServiceApi serviceApi;
    private List<ServiceType> selectableEntities;
    private List<ServiceType> selectedEntities;

    public TypeFilter(ITrustedServiceApi serviceApi) {
        this.serviceApi = serviceApi;
        this.selectableEntities = serviceApi.GetServiceTypes(null);
        this.selectedEntities = new ArrayList<>();
    }

    public void SelectEntity(String typeName){
        Optional<ServiceType> c = selectableEntities.stream().filter(x -> x.getName().equals(typeName)).findFirst();
        if(c.isPresent()){
            selectableEntities.remove(c.get());
            selectedEntities.add(c.get());
            setSelectable();
            setSelected();
        }
    }

    public void DeselectEntity(String typeName){
        Optional<ServiceType> c = selectedEntities.stream().filter(x -> x.getName().equals(typeName)).findFirst();
        if(c.isPresent()){
            selectableEntities.add(c.get());
            selectedEntities.remove(c.get());
            setSelectable();
            setSelected();
        }
    }

    public void FilterSelectableEntities(Filter filter) {
        /*Creo un filtro dove non popolo i types,
          altrimenti filtrerei anche per i types attualmente selezionati.
          Questo non va bene perchè così facendo i types selezionabili
          sarebbero sempre al più uguali ai types selezionati.
         */
        if(filter == null)
            throw new IllegalArgumentException();
        Filter selectableFilter = new Filter();
        selectableFilter.setCountries(filter.getCountries());
        selectableFilter.setProviders(filter.getProviders());
        selectableFilter.setStatuses(filter.getStatuses());
        List<ServiceType> newSelectableEntities = serviceApi.GetServiceTypes(selectableFilter);
        //Rimuoviamo quelli già selezionati
        selectableEntities = newSelectableEntities
                .stream()
                .filter(c -> !selectedEntities.contains(c))
                .collect(Collectors.toList());
        setSelectable();
    }

    public List<ServiceType> getSelectableEntities(){
        return selectableEntities;
    }

    public List<ServiceType> getSelectedEntities(){
        return selectedEntities;
    }
}
