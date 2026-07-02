package com.gabsdev.findaseat.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "tb_users")
@Builder
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String password;
    private List<String> roles;
    @OneToOne
    @JoinColumn(name = "tb_employees_id")
    private Employee employees;

    public User() {
    }

    public User(String email, String password, List<String> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String email, String password, List<String> roles, Employee employee) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.employees = employee;
    }

    public User(UUID id, String email, String password, List<String> role, Employee employees) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.employees = employees;
    }
}
