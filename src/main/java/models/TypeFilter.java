package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TypeFilter {
    private ITrustedServiceApi serviceApi;
    private List<ServiceType> selectableEntities;
    private List<ServiceType> selectedEntities;

    public TypeFilter(ITrustedServiceApi serviceApi) throws IOException {
        this.serviceApi = serviceApi;
        this.selectableEntities = serviceApi.GetServiceTypes(null);
        this.selectedEntities = new ArrayList<>();
    }

    public void SelectEntity(String typeName){
        Optional<ServiceType> c = selectableEntities.stream().filter(x -> x.getName().equals(typeName)).findFirst();
        if(c.isPresent()){
            selectableEntities.remove(c.get());
            selectedEntities.add(c.get());
            //TODO: spara evento per aggiornare sia UI che avvisare ServiceFilter di aggiornare altri Filters
        }
    }

    public void DeselectEntity(String typeName){
        Optional<ServiceType> c = selectedEntities.stream().filter(x -> x.getName().equals(typeName)).findFirst();
        if(c.isPresent()){
            selectableEntities.add(c.get());
            selectedEntities.remove(c.get());
            //TODO: spara evento per aggiornare sia UI che avvisare ServiceFilter di aggiornare altri Filters
        }
    }

    public void FilterSelectableEntities(Filter filter) throws IOException {
        List<ServiceType> newSelectableEntities = serviceApi.GetServiceTypes(filter);
        selectableEntities = newSelectableEntities;

        //TODO: spara evento aggiornamento UI
    }

    public List<ServiceType> getSelectableEntities(){
        return selectableEntities;
    }

    public List<ServiceType> getSelectedEntities(){
        return selectedEntities;
    }
}
