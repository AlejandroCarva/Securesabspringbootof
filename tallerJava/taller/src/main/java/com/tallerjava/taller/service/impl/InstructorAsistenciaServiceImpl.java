package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.dto.GuardarAsistenciasDTO;
import com.tallerjava.taller.model.AsistenciaAmbiente;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.model.Competencia;
import com.tallerjava.taller.repository.IAsistenciaRepository;
import com.tallerjava.taller.repository.IUsuarioRepository;
import com.tallerjava.taller.repository.CompetenciaRepository;
import com.tallerjava.taller.service.IInstructorAsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstructorAsistenciaServiceImpl implements IInstructorAsistenciaService {

    @Autowired
    private IAsistenciaRepository asistenciaRepository;
    
    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Autowired
    private CompetenciaRepository competenciaRepository;

    @Override
    public Map<Integer, String> obtenerAsistenciasPorFechaYFicha(LocalDate fecha, Integer fichaId, Integer instructorId) {
        System.out.println("? SERVICE: Obtener asistencias por fecha y ficha");
        System.out.println("? Fecha: " + fecha + ", Ficha ID: " + fichaId + ", Instructor ID: " + instructorId);
        
        Map<Integer, String> asistenciasMap = new HashMap<>();
        
        try {
            // Obtener aprendices de la ficha
            List<Usuario> aprendices = usuarioRepository.findByFichaId(fichaId);
            System.out.println("? Aprendices en ficha: " + aprendices.size());
            
            for (Usuario aprendiz : aprendices) {
                // Buscar asistencia existente
                Optional<AsistenciaAmbiente> asistenciaOpt = asistenciaRepository
                    .findByUsuarioIdAndFechaAndInstructor(aprendiz.getIdUsuario(), fecha, instructorId);
                
                if (asistenciaOpt.isPresent()) {
                    asistenciasMap.put(aprendiz.getIdUsuario(), asistenciaOpt.get().getEstado());
                    System.out.println("?   - Aprendiz " + aprendiz.getIdUsuario() + ": " + asistenciaOpt.get().getEstado());
                }
            }
            
        } catch (Exception e) {
            System.err.println("? ERROR al obtener asistencias: " + e.getMessage());
            e.printStackTrace();
        }
        
        return asistenciasMap;
    }

    @Override
    @Transactional
    public void guardarAsistencias(GuardarAsistenciasDTO dto) {
        System.out.println("? ======= SERVICE: GUARDAR ASISTENCIAS =======");
        System.out.println("? Ficha: " + dto.getFichaId());
        System.out.println("? Fecha: " + dto.getFechaAsistencia());
        System.out.println("? Competencia: " + dto.getCompetenciaId());
        System.out.println("? Instructor: " + dto.getInstructorId());
        
        if (dto.getAsistencias() == null || dto.getAsistencias().isEmpty()) {
            System.out.println("? ⚠️ No hay asistencias para guardar");
            return;
        }
        
        System.out.println("? Total asistencias recibidas: " + dto.getAsistencias().size());
        
        try {
            // Obtener objetos completos una sola vez
            Usuario instructor = usuarioRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado"));
            
            Competencia competencia = competenciaRepository.findById(dto.getCompetenciaId())
                .orElseThrow(() -> new RuntimeException("Competencia no encontrada"));
            
            System.out.println("? Instructor: " + instructor.getNombre());
            System.out.println("? Competencia: " + competencia.getNombreCompetencia());
            
            int guardadas = 0;
            int errores = 0;
            
            for (Map.Entry<Integer, String> entry : dto.getAsistencias().entrySet()) {
                Integer aprendizId = entry.getKey();
                String estado = entry.getValue();
                
                System.out.println("? Procesando aprendiz " + aprendizId + ": " + estado);
                
                try {
                    Usuario aprendiz = usuarioRepository.findById(aprendizId)
                        .orElseThrow(() -> new RuntimeException("Aprendiz " + aprendizId + " no encontrado"));
                    
                    // Verificar si ya existe
                    Optional<AsistenciaAmbiente> asistenciaExistente = asistenciaRepository
                        .findByUsuarioIdAndFechaAndInstructor(aprendizId, dto.getFechaAsistencia(), dto.getInstructorId());
                    
                    AsistenciaAmbiente asistencia;
                    
                    if (asistenciaExistente.isPresent()) {
                        // Actualizar existente
                        asistencia = asistenciaExistente.get();
                        System.out.println("?   Actualizando asistencia existente ID: " + asistencia.getId());
                    } else {
                        // Crear nueva
                        asistencia = new AsistenciaAmbiente();
                        asistencia.setUsuario(aprendiz);
                        asistencia.setInstructor(instructor);
                        asistencia.setCompetencia(competencia);
                        asistencia.setFecha(dto.getFechaAsistencia());
                        System.out.println("?   Creando nueva asistencia");
                    }
                    
                    asistencia.setEstado(estado);
                    asistenciaRepository.save(asistencia);
                    
                    guardadas++;
                    System.out.println("?   ✅ Guardado exitoso");
                    
                } catch (Exception e) {
                    errores++;
                    System.err.println("?   ❌ Error con aprendiz " + aprendizId + ": " + e.getMessage());
                }
            }
            
            System.out.println("? ======= RESUMEN =======");
            System.out.println("? Total procesadas: " + dto.getAsistencias().size());
            System.out.println("? Guardadas exitosamente: " + guardadas);
            System.out.println("? Errores: " + errores);
            
            // Forzar flush para verificar en BD inmediatamente
            asistenciaRepository.flush();
            System.out.println("? ✅ Flush realizado a BD");
            
        } catch (Exception e) {
            System.err.println("? ❌ ERROR GENERAL en guardarAsistencias: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzar para que el Controller lo capture
        }
    }

    @Override
    public Map<String, Object> obtenerReporteAsistencias(Integer fichaId, Integer instructorId, 
                                                         LocalDate fechaInicio, LocalDate fechaFin) {
        System.out.println("? SERVICE: Obtener reporte de asistencias");
        System.out.println("? Ficha: " + fichaId + ", Instructor: " + instructorId);
        System.out.println("? Fecha inicio: " + fechaInicio + ", Fecha fin: " + fechaFin);
        
        Map<String, Object> reporte = new HashMap<>();
        
        try {
            // 1. Obtener aprendices de la ficha
            List<Usuario> aprendices = usuarioRepository.findByFichaId(fichaId);
            System.out.println("? Aprendices encontrados: " + aprendices.size());
            
            if (aprendices.isEmpty()) {
                System.out.println("? ⚠️ No hay aprendices en esta ficha");
                reporte.put("resultados", new ArrayList<>());
                reporte.put("fechasConRegistros", new ArrayList<>());
                return reporte;
            }
            
            // 2. Extraer IDs de aprendices
            List<Integer> usuarioIds = aprendices.stream()
                .map(Usuario::getIdUsuario)
                .collect(Collectors.toList());
            
            // 3. Obtener todas las asistencias en el rango de fechas
            List<AsistenciaAmbiente> asistencias = asistenciaRepository
                .findByFechaBetweenAndUsuarioIdsAndInstructor(fechaInicio, fechaFin, usuarioIds, instructorId);
            System.out.println("? Asistencias encontradas en rango: " + asistencias.size());
            
            // 4. Obtener fechas únicas con registros
            List<LocalDate> fechasConRegistros = asistencias.stream()
                .map(AsistenciaAmbiente::getFecha)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            System.out.println("? Fechas con registros: " + fechasConRegistros.size());
            
            // 5. Preparar estructura de datos para la vista
            List<Map<String, Object>> resultados = new ArrayList<>();
            
            for (Usuario aprendiz : aprendices) {
                Map<String, Object> aprendizData = new HashMap<>();
                aprendizData.put("usuario", aprendiz);
                
                // Mapa de asistencias por fecha
                Map<String, AsistenciaAmbiente> asistenciasMap = new HashMap<>();
                int totalAsistencias = 0;
                
                // Obtener asistencias de este aprendiz
                List<AsistenciaAmbiente> asistenciasAprendiz = asistencias.stream()
                    .filter(a -> a.getUsuario().getIdUsuario().equals(aprendiz.getIdUsuario()))
                    .collect(Collectors.toList());
                
                // Mapear asistencias por fecha
                for (AsistenciaAmbiente asistencia : asistenciasAprendiz) {
                    String fechaStr = asistencia.getFecha().toString();
                    asistenciasMap.put(fechaStr, asistencia);
                    
                    if ("Asistio".equals(asistencia.getEstado())) {
                        totalAsistencias++;
                    }
                }
                
                // Agregar todas las fechas (incluso las sin registro)
                for (LocalDate fecha : fechasConRegistros) {
                    String fechaStr = fecha.toString();
                    if (!asistenciasMap.containsKey(fechaStr)) {
                        asistenciasMap.put(fechaStr, null);
                    }
                }
                
                aprendizData.put("asistencias", asistenciasMap);
                aprendizData.put("totalAsistencias", totalAsistencias);
                
                resultados.add(aprendizData);
            }
            
            // 6. Agregar datos al reporte
            reporte.put("resultados", resultados);
            reporte.put("fechasConRegistros", fechasConRegistros);
            
            System.out.println("? ✅ Reporte generado exitosamente");
            System.out.println("? - Resultados: " + resultados.size());
            System.out.println("? - Fechas: " + fechasConRegistros.size());
            
        } catch (Exception e) {
            System.err.println("? ❌ ERROR en obtenerReporteAsistencias: " + e.getMessage());
            e.printStackTrace();
            reporte.put("error", e.getMessage());
        }
        
        return reporte;
    }
}