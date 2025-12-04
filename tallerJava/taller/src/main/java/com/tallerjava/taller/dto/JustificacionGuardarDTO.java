package com.tallerjava.taller.dto;

public class JustificacionGuardarDTO {
    private String documentoIdentidad;
    private String fechaSolicitud;
    private String fechaInasistencia;
    private String motivo;
    private String observaciones;
    private String competencia;
    
    // Constructor vacío
    public JustificacionGuardarDTO() {}
    
    // Constructor con parámetros
    public JustificacionGuardarDTO(String documentoIdentidad, String fechaSolicitud, String fechaInasistencia, 
                                  String motivo, String observaciones, String competencia) {
        this.documentoIdentidad = documentoIdentidad;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaInasistencia = fechaInasistencia;
        this.motivo = motivo;
        this.observaciones = observaciones;
        this.competencia = competencia;
    }
    
    // Getters y Setters
    public String getDocumentoIdentidad() { 
        return documentoIdentidad; 
    }
    
    public void setDocumentoIdentidad(String documentoIdentidad) { 
        this.documentoIdentidad = documentoIdentidad; 
    }
    
    public String getFechaSolicitud() { 
        return fechaSolicitud; 
    }
    
    public void setFechaSolicitud(String fechaSolicitud) { 
        this.fechaSolicitud = fechaSolicitud; 
    }
    
    public String getFechaInasistencia() { 
        return fechaInasistencia; 
    }
    
    public void setFechaInasistencia(String fechaInasistencia) { 
        this.fechaInasistencia = fechaInasistencia; 
    }
    
    public String getMotivo() { 
        return motivo; 
    }
    
    public void setMotivo(String motivo) { 
        this.motivo = motivo; 
    }
    
    public String getObservaciones() { 
        return observaciones; 
    }
    
    public void setObservaciones(String observaciones) { 
        this.observaciones = observaciones; 
    }
    
    public String getCompetencia() { 
        return competencia; 
    }
    
    public void setCompetencia(String competencia) { 
        this.competencia = competencia; 
    }
    
    @Override
    public String toString() {
        return "JustificacionGuardarDTO{" +
                "documentoIdentidad='" + documentoIdentidad + '\'' +
                ", fechaSolicitud='" + fechaSolicitud + '\'' +
                ", fechaInasistencia='" + fechaInasistencia + '\'' +
                ", motivo='" + motivo + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", competencia='" + competencia + '\'' +
                '}';
    }
}