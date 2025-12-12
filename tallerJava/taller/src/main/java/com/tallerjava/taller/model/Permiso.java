package com.tallerjava.taller.model;


import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "permissions")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "guard_name")
    private String guardName;

    @ManyToMany
    @JoinTable(
            name = "role_has_permissions",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<com.tallerjava.taller.model.Rol> roles;

    // GETTERS Y SETTERS
}


