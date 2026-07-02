package com.gabsdev.findaseat.model.entity;

import jakarta.persistence.*;


@Embeddable
public class Location {


    private String country;
    private String city;
    private String address;
    private String postalCode;

    public Location() {
    }

    public Location(String country, String city, String address, String postalCode) {
        this.country = country;
        this.city = city;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
