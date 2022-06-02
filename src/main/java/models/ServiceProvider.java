package models;

import utils.Subject;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceProvider extends Subject {
    private int tspId;
    private String name;
    private String countryCode;
    private List<String> qServiceTypes;
    private List<Service> services;

    public List<Service> getServices(){
        return services;
    }

    public List<String> getStatuses(){
        return services
                .stream()
                .map(x -> x.getCurrentStatus())
                .distinct()
                .collect(Collectors.toList());
    }
    public int getId() {
        return tspId;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public List<String> getqServiceTypes() {
        return qServiceTypes;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj instanceof ServiceProvider) {
            ServiceProvider provider = (ServiceProvider) obj;
            return this.tspId == provider.getId()
                    && this.countryCode.equals(provider.getCountryCode());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return (this.tspId+this.countryCode).hashCode();
    }
}
