package models;

public class Country {
    private String countryCode;
    private String countryName;

    public String GetCountryCode(){
        return countryCode;
    }

    public String GetCountryName(){
        return countryName;
    }
    public Country(){

    }

    public Country(String countryCode, String countryName){
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj instanceof Country)
            return this.countryCode.equals(((Country) obj).GetCountryCode());
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.countryCode.hashCode();
    }
}
