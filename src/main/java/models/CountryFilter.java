package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CountryFilter {
    private ITrustedServiceApi serviceApi;
    private List<Country> selectableEntities;
    private List<Country> selectedEntities;

    //TODO: aggiungi eventi modifica selectable e selected
    // Usare subscriber pattern o observer pattern per sparare gli eventi
    // Avrò un evento SelectedUpdated per notificare di eventuali modifiche alle entità selezionate (aggiunta/rimozione)
    // e un altro SelectableUpdated per notificare di eventuali modifiche alle entità selezionabili (aggiunta/rimozione)
    // SelectedUpdated interesserà i controller (aggiornamento view) e il service filter (filtraggio altri filter)
    // SelectableUpdated interesserà solamente i controller (aggiornamento view)

    //TODO: il controller gestisce la IOException e segnala l'errore alla Ui in modo che l'utente lo veda
    public CountryFilter(ITrustedServiceApi serviceApi) throws IOException {
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

    public void SelectCountry(String countryName){
        Optional<Country> c = selectableEntities.stream().filter(x -> x.GetCountryName().equals(countryName)).findFirst();
        if(c.isPresent()){
            selectableEntities.remove(c.get());
            selectedEntities.add(c.get());
            //TODO: spara evento SelectedUpdated e SelectableUpdated
        }
    }

    public void DeselectCountry(String countryName){
        Optional<Country> c = selectedEntities.stream().filter(x -> x.GetCountryName().equals(countryName)).findFirst();
        if(c.isPresent()) {
            selectableEntities.add(c.get());
            selectedEntities.remove(c.get());
            //TODO: spara evento SelectedUpdated e SelectableUpdated
        }
    }


    //TODO: il controller gestisce la IOException e segnala l'errore alla Ui in modo che l'utente lo veda
    public void FilterSelectableEntities(Filter filter) throws IOException {
        List<Country> newSelectableEntities = serviceApi.GetCountries(filter);
        selectableEntities = newSelectableEntities;

        //TODO: spara evento SelectableUpdated
    }
}
