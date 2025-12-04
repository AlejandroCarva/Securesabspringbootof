package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.util.Set;

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

    // Opcional: relación inversa (no necesaria si no la usas)
    @ManyToMany(mappedBy = "roles")
    private Set<Usuario> usuarios;

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

    public Set<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(Set<Usuario> usuarios) { this.usuarios = usuarios; }
}