package models;

import utils.Observer;
import utils.Subject;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceFilter extends Observer {
    private ITrustedServiceApi serviceApi;
    private CountryFilter countryFilter;
    private ProviderFilter providerFilter;
    private TypeFilter typeFilter;
    private StatusFilter statusFilter;

    public ServiceFilter(ITrustedServiceApi serviceApi,
                         CountryFilter countryFilter,
                         ProviderFilter providerFilter,
                         TypeFilter typeFilter,
                         StatusFilter statusFilter){
        this.serviceApi = serviceApi;
        this.countryFilter = countryFilter;
        this.providerFilter = providerFilter;
        this.typeFilter = typeFilter;
        this.statusFilter = statusFilter;
        countryFilter.attach(this);
        providerFilter.attach(this);
        providerFilter.attach(this);
        typeFilter.attach(this);
    }

    public List<Service> getServices(){
        Filter f = new Filter();
        f.setStatuses(statusFilter.getSelectedEntities());
        f.setTypes(typeFilter.getSelectedEntities());
        f.setProviders(providerFilter.getSelectedEntities());
        f.setCountries(countryFilter.getSelectedEntities());
        return serviceApi.GetServices(f);
    }

    public void HandleCountryChange() {
        //Andiamo ad aggiornare gli altri filtri
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities());
        f.setTypes(typeFilter.getSelectedEntities());
        f.setProviders(providerFilter.getSelectedEntities());
        f.setCountries(countryFilter.getSelectedEntities());

        statusFilter.FilterSelectableEntities(f);
        providerFilter.FilterSelectableEntities(f);
        typeFilter.FilterSelectableEntities(f);
    }

    public void HandleStatusChange() {
        //Andiamo ad aggiornare gli altri filtri
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities());
        f.setTypes(typeFilter.getSelectedEntities());
        f.setProviders(providerFilter.getSelectedEntities());
        f.setCountries(countryFilter.getSelectedEntities());

        countryFilter.FilterSelectableEntities(f);
        providerFilter.FilterSelectableEntities(f);
        typeFilter.FilterSelectableEntities(f);
    }

    public void HandleTypeChange() {
        //Andiamo ad aggiornare gli altri filtri
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities());
        f.setTypes(typeFilter.getSelectedEntities());
        f.setProviders(providerFilter.getSelectedEntities());
        f.setCountries(countryFilter.getSelectedEntities());

        statusFilter.FilterSelectableEntities(f);
        providerFilter.FilterSelectableEntities(f);
        countryFilter.FilterSelectableEntities(f);
    }

    public void HandleProviderChange() {
        //Andiamo ad aggiornare gli altri filtri
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities());
        f.setTypes(typeFilter.getSelectedEntities());
        f.setProviders(providerFilter.getSelectedEntities());
        f.setCountries(countryFilter.getSelectedEntities());

        statusFilter.FilterSelectableEntities(f);
        countryFilter.FilterSelectableEntities(f);
        typeFilter.FilterSelectableEntities(f);
    }

    @Override
    public void updateSelected(Subject s) {
        //L'evento di update porta con se l'istanza del subject che lo ha emeso
        //Verifichiamo il tipo della sottoclasse che l'ha emesso per capire come gestirlo
        if(s instanceof CountryFilter) {
            HandleCountryChange();
        }
        if(s instanceof ProviderFilter){
            HandleProviderChange();
        }
        if(s instanceof StatusFilter){
            HandleStatusChange();
        }
        if(s instanceof TypeFilter){
            HandleTypeChange();
        }
    }

    @Override
    public void updateSelectable(Subject s) {
        //Non mi interessa, non fare nulla
    }
}
