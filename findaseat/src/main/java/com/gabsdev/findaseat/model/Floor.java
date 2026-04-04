package com.gabsdev.findaseat.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_floors")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String towerName;
    private String floorName;
    private String slug;
    @ManyToOne
    @JoinColumn(name="tb_business_id")
    private Business business;

    public Floor() {
    }

    public Floor(String floorName, String towerName) {
        this.floorName = floorName;
        this.towerName = towerName;
    }

    public Floor(String floorName, String slug, String towerName) {
        this.floorName = floorName;
        this.slug = slug;
        this.towerName = towerName;
    }

    public Floor(UUID id, String floorName, String slug, Business business, List<Seat> seatList, String towerName) {
        this.id = id;
        this.floorName = floorName;
        this.slug = slug;
        this.business = business;
        this.towerName = towerName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTowerName() {
        return towerName;
    }

    public void setTowerName(String towerName) {
        this.towerName = towerName;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
