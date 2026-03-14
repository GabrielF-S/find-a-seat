package com.gabsdev.findaseat.model;

import jakarta.persistence.*;

@Embeddable
public class Location {


    private String country;
    private String city;
    private String adress;
    private String postalCode;

    public Location() {
    }

    public Location(String country, String city, String adress, String postalCode) {
        this.country = country;
        this.city = city;
        this.adress = adress;
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
