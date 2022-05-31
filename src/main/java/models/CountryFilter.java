package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CountryFilter {
    private ITrustedServiceApi serviceApi;
    private List<Country> selectableEntities;
    private List<Country> selectedEntities;

    public CountryFilter(ITrustedServiceApi serviceApi){
        this.serviceApi = serviceApi;
        try {
            this.selectableEntities = serviceApi.GetCountries(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.selectedEntities = new ArrayList<>();
    }

    public List<Country> getSelectableEntities() {
        return selectableEntities;
    }

    public List<Country> getSelectedEntities() {
        return selectedEntities;
    }

    public void SelectCountry(String countryName){
        Optional<Country> c = selectableEntities.stream().filter(x -> x.GetCountryName() == countryName).findFirst();
        if(c.isPresent()){
            selectableEntities.remove(c.get());
            selectedEntities.add(c.get());
            //TODO: spara evento per aggiornare sia UI che avvisare ServiceFilter di aggiornare altri Filters
        }
    }

    public void DeselectCountry(String countryName){
        Optional<Country> c = selectedEntities.stream().filter(x -> x.GetCountryName() == countryName).findFirst();
        if(c.isPresent()) {
            selectableEntities.add(c.get());
            selectedEntities.remove(c.get());
            //TODO: spara evento per aggiornare sia UI che avvisare ServiceFilter di aggiornare altri Filters
        }
    }

    public void FilterSelectableEntities(Filter filter){
        //TODO: implementa filtro
        //TODO: spara evento aggiornamento UI
    }
}
