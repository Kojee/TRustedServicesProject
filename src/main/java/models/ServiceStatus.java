package models;

import utils.Subject;

public class ServiceStatus {
    private String status;
    public ServiceStatus(String status){
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
