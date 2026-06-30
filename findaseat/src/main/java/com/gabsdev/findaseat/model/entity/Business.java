package com.gabsdev.findaseat.model.entity;

import com.gabsdev.findaseat.model.enums.BusinessType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "tb_business")
@Data
@Builder
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String businessName;
    @Embedded
    private Location location;
    private BusinessType businessType;

    public Business() {

    }


    public Business(String businessName, Location location) {
        this.businessName = businessName;
        this.location = location;
    }

    public Business(UUID uuid, String businessName, Location location) {
        this.uuid = uuid;
        this.businessName = businessName;
        this.location = location;
    }
}
