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
    @JoinColumn(name = "tb_buildings.id")
    private List<Building> buildings;

    public Business() {
    }

    public Business(String businessName, List<Employees> employees, List<Building> buildings) {
        this.businessName = businessName;
        this.employees = employees;
        this.buildings = buildings;
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

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
}
