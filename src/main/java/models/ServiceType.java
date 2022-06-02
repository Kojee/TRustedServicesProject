package models;

public class ServiceType {
    private String name;

    public ServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        return this.getName().equals(((ServiceType)obj).getName());

    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
