package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "guard_name")
    private String guardName;

    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios;

    protected Rol() {
        // Constructor por defecto requerido por JPA
    }

    public Rol(Long id, String name, String guardName, List<Usuario> usuarios) {
        this.id = id;
        this.name = name;
        this.guardName = guardName;
        this.usuarios = usuarios;
    }

    // GETTERS Y SETTERS


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuardName() {
        return guardName;
    }

    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }

    public java.util.List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(java.util.List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
