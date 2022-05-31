package models;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceProvider {
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
}
