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

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj instanceof ServiceStatus) {
            ServiceStatus status = (ServiceStatus) obj;
            return this.status.equals(status.getStatus());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.status.hashCode();
    }
}
