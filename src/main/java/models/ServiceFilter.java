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
        f.setStatuses(statusFilter.getSelectedEntities().stream().map(s -> s.getStatus()).collect(Collectors.toList()));
        f.setTypes(typeFilter.getSelectedEntities().stream().map(t -> t.getName()).collect(Collectors.toList()));
        f.setProviders(providerFilter.getSelectedEntities().stream().map(p -> p.getName()).collect(Collectors.toList()));
        f.setCountries(countryFilter.getSelectedEntities().stream().map(c -> c.GetCountryName()).collect(Collectors.toList()));
        return serviceApi.GetServices(f);
    }

    public void HandleCountryChange() throws IOException {
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

    public void HandleStatusChange() throws IOException {
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

    public void HandleTypeChange() throws IOException {
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

    public void HandleProviderChange() throws IOException {
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

    @Override
    public void updateSelected(Subject s) {
        //L'evento di update porta con se l'istanza del subject che lo ha emeso
        //Verifichiamo il tipo della sottoclasse che l'ha emesso per capire come gestirlo
        if(s instanceof CountryFilter) {
            try {
                HandleCountryChange();
            } catch (IOException e) {
                //TODO: notify controller of error
            }
        }
        if(s instanceof ProviderFilter){
            try {
                HandleProviderChange();
            } catch (IOException e) {
                //TODO: notify controller of error
            }
        }
        if(s instanceof StatusFilter){
            try {
                HandleStatusChange();
            } catch (IOException e) {
                //TODO: notify controller of error
            }
        }
        if(s instanceof TypeFilter){
            try {
                HandleTypeChange();
            } catch (IOException e) {
                //TODO: notify controller of error
            }
        }
    }

    @Override
    public void updateSelectable(Subject s) {
        //Non mi interessa, non fare nulla
    }
}
