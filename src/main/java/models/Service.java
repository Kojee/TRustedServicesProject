package models;

import java.util.List;

public class Service {
    private int tspId;
    private int serviceId;
    private String countryCode;
    private String serviceName;
    private String currentStatus;
    private List<String> qServiceTypes;

    public Service() {
    }

    public int getTspId() {
        return tspId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public List<String> getqServiceTypes() {
        return qServiceTypes;
    }
}
