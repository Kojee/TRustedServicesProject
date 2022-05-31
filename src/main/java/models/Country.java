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
}
