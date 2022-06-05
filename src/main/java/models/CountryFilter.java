package models;

import utils.Subject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CountryFilter extends Subject {
    private ITrustedServiceApi serviceApi;
    private List<Country> selectableEntities;
    private List<Country> selectedEntities;

    //TODO: il controller gestisce la IOException e segnala l'errore alla Ui in modo che l'utente lo veda
    public CountryFilter(ITrustedServiceApi serviceApi) {
        this.serviceApi = serviceApi;
        this.selectableEntities = serviceApi.GetCountries(null);
        this.selectedEntities = new ArrayList<>();
    }

    public List<Country> getSelectableEntities() {
        return selectableEntities;
    }

    public List<Country> getSelectedEntities() {
        return selectedEntities;
    }

    public void SelectEntity(String countryName){
        Optional<Country> c = selectableEntities.stream().filter(x -> x.GetCountryName().equals(countryName)).findFirst();
        if(c.isPresent()){
            selectableEntities.remove(c.get());
            selectedEntities.add(c.get());
            setSelectable();
            setSelected();
        }
    }

    public void DeselectEntity(String countryName){
        Optional<Country> c = selectedEntities.stream().filter(x -> x.GetCountryName().equals(countryName)).findFirst();
        if(c.isPresent()) {
            selectableEntities.add(c.get());
            selectedEntities.remove(c.get());
            setSelectable();
            setSelected();
        }
    }


    //TODO: il controller gestisce la IOException e segnala l'errore alla Ui in modo che l'utente lo veda
    public void FilterSelectableEntities(Filter filter) {
        /*Creo un filtro dove non popolo le countries,
          altrimenti filterei anche per le country attualmente selezionate.
          Questo non va bene perchè così facendo le country selezionabili
          sarebbero sempre al più uguali alle country selezionate.
         */
        if(filter == null)
            throw new IllegalArgumentException();
        Filter selectableFilter = new Filter();
        selectableFilter.setProviders(filter.getProviders());
        selectableFilter.setTypes(filter.getTypes());
        selectableFilter.setStatuses(filter.getStatuses());
        List<Country> newSelectableEntities = serviceApi.GetCountries(selectableFilter);
        //Rimuoviamo quelle già selezionate
        selectableEntities = newSelectableEntities
                .stream()
                .filter(c -> !selectedEntities.contains(c))
                .collect(Collectors.toList());
        setSelectable();
    }
}
