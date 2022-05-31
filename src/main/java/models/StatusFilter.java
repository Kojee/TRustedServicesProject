package models;

import views.StatusView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StatusFilter {
    private ITrustedServiceApi serviceApi;
    private List<ServiceStatus> selectableEntities;
    private List<ServiceStatus> selectedEntities;

    public StatusFilter(ITrustedServiceApi serviceApi) throws IOException {
        this.serviceApi = serviceApi;
        this.selectableEntities = serviceApi.GetServiceStatuses(null);
        this.selectedEntities = new ArrayList<>();
    }

    public void SelectEntity(String statusName){
        Optional<ServiceStatus> c = selectableEntities.stream().filter(x -> x.getStatus().equals(statusName)).findFirst();
        if(c.isPresent()){
            selectableEntities.remove(c.get());
            selectedEntities.add(c.get());
            //TODO: spara evento per aggiornare sia UI che avvisare ServiceFilter di aggiornare altri Filters
        }
    }

    public void DeselectEntity(String statusName){
        Optional<ServiceStatus> c = selectedEntities.stream().filter(x -> x.getStatus().equals(statusName)).findFirst();
        if(c.isPresent()){
            selectableEntities.add(c.get());
            selectedEntities.remove(c.get());
            //TODO: spara evento per aggiornare sia UI che avvisare ServiceFilter di aggiornare altri Filters
        }
    }

    public void FilterSelectableEntities(Filter filter) throws IOException {
        List<ServiceStatus> newSelectableEntities = serviceApi.GetServiceStatuses(filter);
        selectableEntities = newSelectableEntities;

        //TODO: spara evento aggiornamento UI
    }

    public List<ServiceStatus> getSelectableEntities(){
        return selectableEntities;
    }

    public List<ServiceStatus> getSelectedEntities(){
        return selectedEntities;
    }
}
