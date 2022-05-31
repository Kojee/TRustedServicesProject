package models;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceFilter {
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
    }

    public List<Service> getServices(){
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities().stream().map(s -> s.getStatus()).collect(Collectors.toList()));
        f.setTypes(typeFilter.getSelectedEntities().stream().map(t -> t.getName()).collect(Collectors.toList()));
        f.setProviders(providerFilter.getSelectedEntities().stream().map(p -> p.getName()).collect(Collectors.toList()));
        f.setCountries(countryFilter.getSelectedEntities().stream().map(c -> c.GetCountryName()).collect(Collectors.toList()));

        return serviceApi.GetServices(f);
    }

    public void HandleCountryChange(Country entity) throws IOException {
        //TODO: questa funzione serve a gestire gli eventi di modifica emessi dal filter delle Country
        //Andiamo ad aggiornare gli altri filtri
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities().stream().map(s -> s.getStatus()).collect(Collectors.toList()));
        f.setTypes(typeFilter.getSelectedEntities().stream().map(t -> t.getName()).collect(Collectors.toList()));
        f.setProviders(providerFilter.getSelectedEntities().stream().map(p -> p.getName()).collect(Collectors.toList()));
        f.setCountries(countryFilter.getSelectedEntities().stream().map(c -> c.GetCountryName()).collect(Collectors.toList()));

        statusFilter.FilterSelectableEntities(f);
        providerFilter.FilterSelectableEntities(f);
        typeFilter.FilterSelectableEntities(f);
    }

    public void HandleStatusChange(ServiceStatus entity) throws IOException {
        //TODO: questa funzione serve a gestire gli eventi di modifica emessi dal filter degli status
        //Andiamo ad aggiornare gli altri filtri
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities().stream().map(s -> s.getStatus()).collect(Collectors.toList()));
        f.setTypes(typeFilter.getSelectedEntities().stream().map(t -> t.getName()).collect(Collectors.toList()));
        f.setProviders(providerFilter.getSelectedEntities().stream().map(p -> p.getName()).collect(Collectors.toList()));
        f.setCountries(countryFilter.getSelectedEntities().stream().map(c -> c.GetCountryName()).collect(Collectors.toList()));

        countryFilter.FilterSelectableEntities(f);
        providerFilter.FilterSelectableEntities(f);
        typeFilter.FilterSelectableEntities(f);
    }

    public void HandleTypeChange(ServiceType entity) throws IOException {
        //TODO: questa funzione serve a gestire gli eventi di modifica emessi dal filter dei types
        //Andiamo ad aggiornare gli altri filtri
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities().stream().map(s -> s.getStatus()).collect(Collectors.toList()));
        f.setTypes(typeFilter.getSelectedEntities().stream().map(t -> t.getName()).collect(Collectors.toList()));
        f.setProviders(providerFilter.getSelectedEntities().stream().map(p -> p.getName()).collect(Collectors.toList()));
        f.setCountries(countryFilter.getSelectedEntities().stream().map(c -> c.GetCountryName()).collect(Collectors.toList()));

        statusFilter.FilterSelectableEntities(f);
        providerFilter.FilterSelectableEntities(f);
        countryFilter.FilterSelectableEntities(f);
    }

    public void HandleProviderChange(ServiceProvider entity) throws IOException {
        //TODO: questa funzione serve a gestire gli eventi di modifica emessi dal filter delle provider
        //Andiamo ad aggiornare gli altri filtri
        Filter f = new Filter();

        f.setStatuses(statusFilter.getSelectedEntities().stream().map(s -> s.getStatus()).collect(Collectors.toList()));
        f.setTypes(typeFilter.getSelectedEntities().stream().map(t -> t.getName()).collect(Collectors.toList()));
        f.setProviders(providerFilter.getSelectedEntities().stream().map(p -> p.getName()).collect(Collectors.toList()));
        f.setCountries(countryFilter.getSelectedEntities().stream().map(c -> c.GetCountryName()).collect(Collectors.toList()));

        statusFilter.FilterSelectableEntities(f);
        countryFilter.FilterSelectableEntities(f);
        typeFilter.FilterSelectableEntities(f);
    }
}
