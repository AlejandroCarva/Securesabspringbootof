package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.Ficha;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.repository.FichaRepository;
import com.tallerjava.taller.repository.IUsuarioRepository;
import com.tallerjava.taller.service.IFichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FichaServiceImpl implements IFichaService {

    @Autowired
    private FichaRepository fichaRepository;
    
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Ficha> obtenerFichasPorInstructor(Integer idInstructor) {
        System.out.println("? FICHA SERVICE - Obtener fichas por instructor: " + idInstructor);
        
        try {
            // ✅ USAR EL MÉTODO NUEVO que ya incluye programa cargado
            List<Ficha> fichas = fichaRepository.findByInstructorIdWithRelations(idInstructor);
            System.out.println("? Fichas encontradas (con relaciones): " + fichas.size());
            
            // ✅ DEBUG DETALLADO
            if (!fichas.isEmpty()) {
                System.out.println("? ======= DETALLE DE FICHAS ENCONTRADAS =======");
                for (int i = 0; i < fichas.size(); i++) {
                    Ficha f = fichas.get(i);
                    System.out.println("? Ficha " + (i + 1) + ":");
                    System.out.println("?   - ID: " + f.getIdFicha());
                    System.out.println("?   - Número: " + f.getNumeroFicha());
                    System.out.println("?   - Estado: " + f.getEstado());
                    System.out.println("?   - Jornada: " + (f.getJornada() != null ? f.getJornada().getNombreJornada() : "null"));
                    System.out.println("?   - Programa: " + (f.getPrograma() != null ? f.getPrograma().getNombrePrograma() : "null"));
                    System.out.println("?   - ID Programa: " + f.getIdPrograma());
                    System.out.println("?   - Instructores: " + (f.getInstructores() != null ? f.getInstructores().size() : 0));
                    
                    // Contar aprendices activos
                    Long aprendicesCount = fichaRepository.countAprendicesByFichaId(f.getIdFicha());
                    System.out.println("?   - Aprendices activos: " + (aprendicesCount != null ? aprendicesCount : 0));
                }
                System.out.println("? ==========================================");
            }
            
            return fichas;
            
        } catch (Exception e) {
            System.err.println("? ERROR en obtenerFichasPorInstructor: " + e.getMessage());
            e.printStackTrace();
            
            // ✅ FALLBACK a consulta nativa si hay error
            try {
                System.out.println("? Intentando con consulta nativa de fallback...");
                List<Ficha> fichasNativas = fichaRepository.findActiveByInstructorId(idInstructor);
                System.out.println("? Fichas nativas encontradas: " + fichasNativas.size());
                
                // Debug de fichas nativas
                for (Ficha f : fichasNativas) {
                    System.out.println("?   - " + f.getNumeroFicha() + 
                                     " (Estado: " + f.getEstado() + 
                                     ", ID Programa: " + f.getIdPrograma() + ")");
                }
                
                return fichasNativas;
            } catch (Exception ex) {
                System.err.println("? ERROR en fallback nativo: " + ex.getMessage());
                return new ArrayList<>();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ficha> obtenerFichasPorInstructorConFiltros(Integer idInstructor, String fichaFilter, String jornadaFilter) {
        System.out.println("? FICHA SERVICE - Obtener fichas con filtros.");
        System.out.println("? Instructor ID: " + idInstructor);
        System.out.println("? Filtro ficha: " + fichaFilter);
        System.out.println("? Filtro jornada: " + jornadaFilter);
        
        try {
            // ✅ USAR EL MÉTODO NUEVO con filtros (ya incluye programa)
            List<Ficha> fichas = fichaRepository.findByInstructorIdWithFilters(
                idInstructor, 
                fichaFilter != null && !fichaFilter.trim().isEmpty() ? fichaFilter : null,
                jornadaFilter != null && !jornadaFilter.trim().isEmpty() ? jornadaFilter : null);
            
            System.out.println("? Fichas encontradas después de filtros: " + fichas.size());
            
            // ✅ DEBUG DETALLADO
            if (!fichas.isEmpty()) {
                System.out.println("? ======= DETALLE DE FICHAS FILTRADAS =======");
                for (Ficha ficha : fichas) {
                    System.out.println("? ✓ Ficha: " + ficha.getNumeroFicha() + 
                        " (ID: " + ficha.getIdFicha() + ")" +
                        ", Programa: " + (ficha.getPrograma() != null ? ficha.getPrograma().getNombrePrograma() : "N/A") +
                        ", Jornada: " + (ficha.getJornada() != null ? ficha.getJornada().getNombreJornada() : "N/A") +
                        ", Estado: " + ficha.getEstado() +
                        ", Instructores: " + (ficha.getInstructores() != null ? ficha.getInstructores().size() : 0));
                }
                System.out.println("? ===========================================");
            }
            
            return fichas;
            
        } catch (Exception e) {
            System.err.println("? ERROR en obtenerFichasPorInstructorConFiltros: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: usar método sin filtros
            System.out.println("? Intentando método fallback...");
            List<Ficha> fichasFallback = obtenerFichasPorInstructor(idInstructor);
            System.out.println("? Fichas fallback: " + fichasFallback.size());
            return fichasFallback;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Ficha obtenerPorId(Integer id) {
        System.out.println("? FICHA SERVICE - Obtener por ID: " + id);
        
        try {
            // ✅ USAR EL MÉTODO NUEVO que carga programa
            Optional<Ficha> fichaOpt = fichaRepository.findByIdWithPrograma(id);
            
            if (fichaOpt.isEmpty()) {
                throw new RuntimeException("Ficha no encontrada con ID: " + id);
            }
            
            Ficha ficha = fichaOpt.get();
            System.out.println("? Ficha encontrada: " + ficha.getNumeroFicha());
            System.out.println("? Programa: " + (ficha.getPrograma() != null ? ficha.getPrograma().getNombrePrograma() : "null"));
            
            return ficha;
            
        } catch (Exception e) {
            System.err.println("? ERROR en obtenerPorId: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Ficha obtenerPorIdConAprendices(Integer id) {
        System.out.println("? FICHA SERVICE - Obtener con aprendices por ID: " + id);
        
        try {
            // ✅ USAR EL MÉTODO NUEVO con todas las relaciones
            Optional<Ficha> fichaOpt = fichaRepository.findByIdWithAllRelations(id);
            
            if (fichaOpt.isEmpty()) {
                throw new RuntimeException("Ficha no encontrada con ID: " + id);
            }
            
            Ficha ficha = fichaOpt.get();
            
            System.out.println("? Ficha encontrada: " + ficha.getNumeroFicha());
            System.out.println("? Programa: " + (ficha.getPrograma() != null ? ficha.getPrograma().getNombrePrograma() : "null"));
            System.out.println("? Aprendices: " + (ficha.getAprendices() != null ? ficha.getAprendices().size() : 0));
            System.out.println("? Jornada: " + (ficha.getJornada() != null ? ficha.getJornada().getNombreJornada() : "null"));
            
            return ficha;
            
        } catch (Exception e) {
            System.err.println("? ERROR en obtenerPorIdConAprendices: " + e.getMessage());
            
            // Fallback al método básico
            try {
                return obtenerPorId(id);
            } catch (Exception ex) {
                throw new RuntimeException("Error al obtener ficha: " + ex.getMessage());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarInstructorTieneAprendiz(Integer idInstructor, Integer idAprendiz) {
        System.out.println("? FICHA SERVICE - Verificar instructor tiene aprendiz");
        System.out.println("? Instructor ID: " + idInstructor + ", Aprendiz ID: " + idAprendiz);
        
        try {
            // Obtener el aprendiz
            Optional<Usuario> aprendizOpt = usuarioRepository.findById(idAprendiz);
            
            if (aprendizOpt.isEmpty()) {
                System.out.println("? Aprendiz no encontrado con ID: " + idAprendiz);
                return false;
            }
            
            Usuario aprendiz = aprendizOpt.get();
            System.out.println("? Aprendiz encontrado: " + aprendiz.getNombre() + " " + aprendiz.getApellido());
            
            if (aprendiz.getFicha() == null) {
                System.out.println("? El aprendiz no tiene ficha asignada");
                return false;
            }
            
            // ✅ Cargar programa en la ficha del aprendiz
            Integer fichaId = aprendiz.getFicha().getIdFicha();
            Optional<Ficha> fichaOpt = fichaRepository.findByIdWithPrograma(fichaId);
            
            if (fichaOpt.isPresent()) {
                aprendiz.setFicha(fichaOpt.get());
                System.out.println("? Ficha del aprendiz: " + aprendiz.getFicha().getNumeroFicha());
                System.out.println("? Programa: " + (aprendiz.getFicha().getPrograma() != null ? 
                    aprendiz.getFicha().getPrograma().getNombrePrograma() : "N/A"));
            }
            
            // Verificar que el instructor está asignado a la ficha del aprendiz
            boolean tieneAsignacion = fichaRepository.existsByFichaIdAndInstructorId(
                aprendiz.getFicha().getIdFicha(), idInstructor);
            
            System.out.println("? Resultado verificación: " + tieneAsignacion);
            return tieneAsignacion;
            
        } catch (Exception e) {
            System.err.println("? ERROR en verificarInstructorTieneAprendiz: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ficha> obtenerFichasActivasPorInstructor(Integer idInstructor) {
        System.out.println("? FICHA SERVICE - Obtener fichas activas por instructor: " + idInstructor);
        
        try {
            // ✅ USAR EL MÉTODO PRINCIPAL (ya filtra por estado 'Activa')
            List<Ficha> fichas = fichaRepository.findByInstructorIdWithRelations(idInstructor);
            System.out.println("? Fichas activas encontradas: " + fichas.size());
            
            // Debug de fichas encontradas
            for (Ficha f : fichas) {
                System.out.println("?   - " + f.getNumeroFicha() + 
                                 " (Estado: " + f.getEstado() + 
                                 ", Programa: " + (f.getPrograma() != null ? f.getPrograma().getNombrePrograma() : "null") + ")");
            }
            
            return fichas;
            
        } catch (Exception e) {
            System.err.println("? ERROR en obtenerFichasActivasPorInstructor: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // ✅ MÉTODO ADICIONAL: Contar aprendices por ficha
    @Transactional(readOnly = true)
    public Long contarAprendicesPorFicha(Integer fichaId) {
        try {
            return fichaRepository.countAprendicesByFichaId(fichaId);
        } catch (Exception e) {
            System.err.println("? ERROR al contar aprendices: " + e.getMessage());
            return 0L;
        }
    }
    
    // ✅ MÉTODO ADICIONAL: Verificar si existe número de ficha
    @Transactional(readOnly = true)
    public boolean existeNumeroFicha(String numeroFicha) {
        try {
            return fichaRepository.existsByNumeroFicha(numeroFicha);
        } catch (Exception e) {
            System.err.println("? ERROR al verificar número de ficha: " + e.getMessage());
            return false;
        }
    }
}