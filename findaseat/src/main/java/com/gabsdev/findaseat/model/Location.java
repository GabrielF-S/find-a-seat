package com.gabsdev.findaseat.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {

    private String country;
    private String city;
    private String adress;
    private String postalCode;
}
