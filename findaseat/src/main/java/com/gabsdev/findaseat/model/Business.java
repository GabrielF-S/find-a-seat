package com.gabsdev.findaseat.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String businessName;
    @OneToMany
    @JoinColumn(name = "tb_employees.id")
    private List<Employees> employees;
    @OneToMany
    @JoinColumn(name = "tb_floors.id")
    private List<Floors> floorsList;
    @Embedded
    private Location location;

    public Business() {

    }

    public Business(String businessName, List<Employees> employees, List<Floors> floorsList, Location location) {
        this.businessName = businessName;
        this.employees = employees;
        this.floorsList = floorsList;
        this.location = location;
    }

    public Business(String businessName, Location location) {
        this.businessName = businessName;
        this.location = location;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<Employees> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employees> employees) {
        this.employees = employees;
    }

    public List<Floors> getFloorsList() {
        return floorsList;
    }

    public void setFloorsList(List<Floors> floorsList) {
        this.floorsList = floorsList;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
