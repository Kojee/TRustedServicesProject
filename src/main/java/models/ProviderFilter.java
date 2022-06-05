package models;

import utils.Subject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProviderFilter extends Subject {
    private ITrustedServiceApi serviceApi;
    private List<ServiceProvider> selectableEntities;
    private List<ServiceProvider> selectedEntities;

    public ProviderFilter(ITrustedServiceApi serviceApi) {
        this.serviceApi = serviceApi;
        this.selectableEntities = serviceApi.GetServiceProviders(null);
        this.selectedEntities = new ArrayList<>();
    }

    public void SelectEntity(String providerName){
        Optional<ServiceProvider> e = selectableEntities.stream().filter(x -> x.getName().equals(providerName)).findFirst();
        if(e.isPresent()){
            selectableEntities.remove(e.get());
            selectedEntities.add(e.get());
            setSelectable();
            setSelected();
        }
    }

    public void DeselectEntity(String providerName){
        Optional<ServiceProvider> e = selectedEntities.stream().filter(x -> x.getName().equals(providerName)).findFirst();
        if(e.isPresent()){
            selectableEntities.add(e.get());
            selectedEntities.remove(e.get());
            setSelectable();
            setSelected();
        }
    }

    public void FilterSelectableEntities(Filter filter) {
        /*Creo un filtro dove non popolo i providers,
          altrimenti filtrerei anche per i providers attualmente selezionati.
          Questo non va bene perchè così facendo i providers selezionabili
          sarebbero sempre al più uguali ai providers selezionati.
         */
        if(filter == null)
            throw new IllegalArgumentException();
        Filter selectableFilter = new Filter();
        selectableFilter.setCountries(filter.getCountries());
        selectableFilter.setTypes(filter.getTypes());
        selectableFilter.setStatuses(filter.getStatuses());

        List<ServiceProvider> newSelectableEntities = serviceApi.GetServiceProviders(selectableFilter);
        //Rimuoviamo quelli già selezionati
        selectableEntities = newSelectableEntities
                .stream()
                .filter(c -> !selectedEntities.contains(c))
                .collect(Collectors.toList());
        setSelectable();
    }
    public List<ServiceProvider> getSelectableEntities(){
        return selectableEntities;
    }

    public List<ServiceProvider> getSelectedEntities(){
        return selectedEntities;
    }
}
