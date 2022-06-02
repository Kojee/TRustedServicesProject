package models;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    //TODO: modificare usando i vari models al posto delle stringhe nelle liste

    private List<Country> countryNames;
    private List<ServiceProvider> providerNames;
    private List<ServiceStatus> statusNames;
    private List<ServiceType> typeNames;


    public List<Country> getCountries() {
        return countryNames;
    }
    public void setCountries(List<Country> countries) { this.countryNames = countries; }

    public List<ServiceProvider> getProviders() {
        return providerNames;
    }
    public void setProviders(List<ServiceProvider> providers) { this.providerNames = providers; }

    public List<ServiceStatus> getStatuses() {
        return statusNames;
    }
    public void setStatuses(List<ServiceStatus> statuses) { this.statusNames = statuses; }

    public List<ServiceType> getTypes() {
        return typeNames;
    }
    public void setTypes(List<ServiceType> types) { this.typeNames = types; }


    public Filter(){
        this.countryNames = new ArrayList<>();
        this.providerNames = new ArrayList<>();
        this.statusNames = new ArrayList<>();
        this.typeNames = new ArrayList<>();
    }
}
