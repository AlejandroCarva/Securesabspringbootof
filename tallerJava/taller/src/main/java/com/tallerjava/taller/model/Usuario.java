package com.tallerjava.taller.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    private String cedula;
    private String correo;

    @Column(name = "email_verified_at")
    private Timestamp emailVerifiedAt;

    private String nombre;
    private String apellido;

    @ManyToOne
    @JoinColumn(name = "id_ficha")
    private Ficha ficha;

    private String telefono;
    private String estado;
    private String password;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "model_has_roles",
            joinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    // Conveniencia: alias `id` para compatibilidad con código existente
    public Integer getId() {
        return this.idUsuario;
    }

    public void setId(Integer id) {
        this.idUsuario = id;
    }
}