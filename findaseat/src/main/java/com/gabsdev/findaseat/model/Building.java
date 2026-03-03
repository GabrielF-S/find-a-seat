package com.gabsdev.findaseat.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "tb_building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String buildingName;
    private List<Floors> floorsList;
    private String city;

    public Building() {
    }

    public Building(String buildingName, List<Floors> floorsList, String city) {
        this.buildingName = buildingName;
        this.floorsList = floorsList;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public List<Floors> getFloorsList() {
        return floorsList;
    }

    public void setFloorsList(List<Floors> floorsList) {
        this.floorsList = floorsList;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
