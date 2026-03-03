package com.gabsdev.findaseat.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_floors")
public class Floors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String floorName;
    private String slug;
    private List<Seat> seatList;

    public Floors() {
    }

    public Floors(String floorName, String slug, List<Seat> seatList) {
        this.floorName = floorName;
        this.slug = slug;
        this.seatList = seatList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }
}
