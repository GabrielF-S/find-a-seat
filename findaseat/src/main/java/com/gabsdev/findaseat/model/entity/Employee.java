package com.gabsdev.findaseat.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "tb_employees")
@Data
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeName;
    private String department;
    private String document;
    @ManyToOne
    @JoinColumn(name="tb_business_id")
    private Business business;


    public Employee() {
    }

    public Employee(Long id, String employeeName, String department, String document, Business business) {
        this.id = id;
        this.employeeName = employeeName;
        this.business = business;
        this.department = department;
        this.document = document;
    }


}
