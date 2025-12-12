package com.tallerjava.taller.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    private LocalDateTime createdAt;

    private Boolean readFlag = false;

    private Integer novedadId;

    private String link;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.readFlag = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getReadFlag() { return readFlag; }
    public void setReadFlag(Boolean readFlag) { this.readFlag = readFlag; }

    public Integer getNovedadId() { return novedadId; }
    public void setNovedadId(Integer novedadId) { this.novedadId = novedadId; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
