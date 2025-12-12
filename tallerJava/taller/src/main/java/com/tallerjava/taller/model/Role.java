package com.tallerjava.taller.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "id")
    private Long id; // ✅ Cambiado a Long (porque en tu BD es BIGINT)

    @Column(name = "name")
    private String name;

    @Column(name = "guard_name")
    private String guardName = "web";


    // -------- CONSTRUCTORES --------
    public Role() {}

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // -------- GETTERS & SETTERS --------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGuardName() { return guardName; }
    public void setGuardName(String guardName) { this.guardName = guardName; }
}