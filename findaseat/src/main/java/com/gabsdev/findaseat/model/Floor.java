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
    private String floorName;
    private String slug;
    @ManyToOne
    @JoinColumn(name="tb_business_id")
    private Business business;

    public Floor() {
    }

    public Floor(String floorName) {
        this.floorName = floorName;
    }

    public Floor(String floorName, String slug) {
        this.floorName = floorName;
        this.slug = slug;
    }

    public Floor(UUID id, String floorName, String slug, Business business, List<Seat> seatList) {
        this.id = id;
        this.floorName = floorName;
        this.slug = slug;
        this.business = business;
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


    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
