package com.gabsdev.findaseat.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_floors")
@Data
@Builder
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String towerName;
    private String floorName;
    private String slug;
    @ManyToOne()
    @JoinColumn(name="tb_business_uuid")
    private Business business;
    private String layout;

    public Floor() {
    }

    public Floor(UUID id, String towerName, String floorName, String slug, Business business) {
        this.id = id;
        this.towerName = towerName;
        this.floorName = floorName;
        this.slug = slug;
        this.business = business;
    }

    public Floor(UUID id, String towerName, String floorName, String slug, Business business, String layout) {
        this.id = id;
        this.towerName = towerName;
        this.floorName = floorName;
        this.slug = slug;
        this.business = business;
        this.layout = layout;
    }
}
