package com.gabsdev.findaseat.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeName;
    @ManyToOne
    @JoinColumn(name="tb_business_id")
    private Business business;


    public Employee() {
    }

    public Employee(Long id, String employeeName, Business business) {
        this.id = id;
        this.employeeName = employeeName;
        this.business = business;
    }

    public Employee(String employeeName, Business business) {
        this.employeeName = employeeName;
        this.business = business;
    }

    public Employee(String employeeName) {
        this.employeeName = employeeName;

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}
