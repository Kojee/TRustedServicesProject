package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProviderFilter {
    private ITrustedServiceApi serviceApi;
    private List<ServiceProvider> selectableEntities;
    private List<ServiceProvider> selectedEntities;

    public ProviderFilter(ITrustedServiceApi serviceApi) throws IOException {
        this.serviceApi = serviceApi;
        this.selectableEntities = serviceApi.GetServiceProviders(null);
        this.selectedEntities = new ArrayList<>();
    }

    public void SelectEntity(String providerName){
        Optional<ServiceProvider> e = selectableEntities.stream().filter(x -> x.getName().equals(providerName)).findFirst();
        if(e.isPresent()){
            selectableEntities.remove(e.get());
            selectedEntities.add(e.get());
            //TODO: spara evento per aggiornare sia UI che avvisare ServiceFilter di aggiornare altri Filters
        }
    }

    public void DeselectEntity(String providerName){
        Optional<ServiceProvider> e = selectedEntities.stream().filter(x -> x.getName().equals(providerName)).findFirst();
        if(e.isPresent()){
            selectableEntities.add(e.get());
            selectedEntities.remove(e.get());
            //TODO: spara evento per aggiornare sia UI che avvisare ServiceFilter di aggiornare altri Filters
        }
    }

    public void FilterSelectableEntities(Filter filter) throws IOException {
        List<ServiceProvider> newSelectableEntities = serviceApi.GetServiceProviders(filter);
        selectableEntities = newSelectableEntities;
        //TODO: spara evento aggiornamento UI
    }
    public List<ServiceProvider> getSelectableEntities(){
        return selectableEntities;
    }

    public List<ServiceProvider> getSelectedEntities(){
        return selectedEntities;
    }
}
