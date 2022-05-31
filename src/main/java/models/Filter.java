package models;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    //TODO: modificare usando i vari models al posto delle stringhe nelle liste

    private List<String> countryNames;
    private List<String> providerNames;
    private List<String> statusNames;
    private List<String> typeNames;


    public List<String> getCountries() {
        return countryNames;
    }
    public void setCountries(List<String> countries) { this.countryNames = countries; }

    public List<String> getProviders() {
        return providerNames;
    }
    public void setProviders(List<String> providers) { this.providerNames = providers; }

    public List<String> getStatuses() {
        return statusNames;
    }
    public void setStatuses(List<String> statuses) { this.statusNames = statuses; }

    public List<String> getTypes() {
        return typeNames;
    }
    public void setTypes(List<String> types) { this.typeNames = types; }


    public Filter(){
        this.countryNames = new ArrayList<>();
        this.providerNames = new ArrayList<>();
        this.statusNames = new ArrayList<>();
        this.typeNames = new ArrayList<>();
    }
}
