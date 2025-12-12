package com.tallerjava.taller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegistroManualForm {

    // Documento del visitante (cedula)
    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "\\d+", message = "La cédula solo debe contener números")
    private String documento;

    // Tipo de movimiento: Ingreso o Salida
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento;

    // Motivo opcional
    private String motivo;

    // ===== Getters & Setters =====

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
