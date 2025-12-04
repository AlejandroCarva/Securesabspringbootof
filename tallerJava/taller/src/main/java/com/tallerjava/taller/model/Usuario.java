package com.tallerjava.taller.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

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
    private String nombre;
    private String apellido;
    
    // Relación con Ficha
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ficha")
    private Ficha ficha;
    
    // ✅ CORREGIDO: Usa la tabla REAL de tu BD (model_has_roles)
    @ManyToMany(fetch = FetchType.EAGER) // ✅ Cambiado a EAGER para cargar roles automáticamente
    @JoinTable(
        name = "model_has_roles", // ✅ TABLA CORRECTA
        joinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    
    private String telefono;
    private String estado;
    private String password;
    
    @Column(name = "foto_perfil")
    private String fotoPerfil;
}