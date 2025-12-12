package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.Ficha;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.repository.FichaRepository;
import com.tallerjava.taller.repository.IUsuarioRepository;
import com.tallerjava.taller.service.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FichaServiceImpl implements FichaService {

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    // ===========================================================
    // MÉTODOS BÁSICOS USADOS POR EL COORDINADOR
    // ===========================================================

    @Override
    @Transactional(readOnly = true)
    public List<Ficha> listar() {
        return fichaRepository.findAll();
    }

    @Override
    @Transactional
    public Ficha guardar(Ficha ficha) {
        return fichaRepository.save(ficha);
    }

    // ===========================================================
    // MÉTODOS AVANZADOS (LOS QUE YA USABAS EN TU APLICACIÓN)
    // ===========================================================

    @Override
    @Transactional(readOnly = true)
    public List<Ficha> obtenerFichasPorInstructor(Integer idInstructor) {
        System.out.println("📌 FICHA SERVICE - Obtener fichas por instructor: " + idInstructor);

        try {
            List<Ficha> fichas = fichaRepository.findByInstructorIdWithRelations(idInstructor);
            System.out.println("📌 Fichas encontradas (con relaciones): " + fichas.size());

            for (Ficha f : fichas) {
                System.out.println(" - Ficha: " + f.getNumeroFicha()
                        + ", Programa: " + (f.getPrograma() != null ? f.getPrograma().getNombrePrograma() : "null"));
            }

            return fichas;

        } catch (Exception e) {
            System.err.println("❌ ERROR obteniendo fichas por instructor: " + e.getMessage());

            try {
                System.out.println("📌 Intentando consulta nativa...");
                return fichaRepository.findActiveByInstructorId(idInstructor);
            } catch (Exception ex) {
                System.err.println("❌ ERROR en fallback nativo: " + ex.getMessage());
                return new ArrayList<>();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ficha> obtenerFichasPorInstructorConFiltros(Integer idInstructor, String fichaFilter, String jornadaFilter) {
        System.out.println("📌 FICHA SERVICE - Obtener fichas con filtros.");

        try {
            List<Ficha> fichas = fichaRepository.findByInstructorIdWithFilters(
                    idInstructor,
                    fichaFilter != null && !fichaFilter.trim().isEmpty() ? fichaFilter : null,
                    jornadaFilter != null && !jornadaFilter.trim().isEmpty() ? jornadaFilter : null
            );

            System.out.println("📌 Fichas filtradas: " + fichas.size());
            return fichas;

        } catch (Exception e) {
            System.err.println("❌ ERROR en obtener fichas filtradas: " + e.getMessage());
            return obtenerFichasPorInstructor(idInstructor);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Ficha obtenerPorId(Integer id) {
        System.out.println("📌 Obtener Ficha por ID: " + id);

        Optional<Ficha> fichaOpt = fichaRepository.findByIdWithPrograma(id);

        if (fichaOpt.isEmpty()) {
            throw new RuntimeException("Ficha no encontrada con ID: " + id);
        }

        return fichaOpt.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Ficha obtenerPorIdConAprendices(Integer id) {
        System.out.println("📌 Obtener Ficha con aprendices por ID: " + id);

        Optional<Ficha> fichaOpt = fichaRepository.findByIdWithAllRelations(id);

        if (fichaOpt.isEmpty()) {
            throw new RuntimeException("Ficha no encontrada con ID: " + id);
        }

        Ficha ficha = fichaOpt.get();

        Long aprendicesCount = fichaRepository.countAprendicesByFichaId(id);
        System.out.println("📌 Aprendices activos: " + aprendicesCount);

        return ficha;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarInstructorTieneAprendiz(Integer idInstructor, Integer idAprendiz) {
        System.out.println("📌 Verificando si el instructor tiene el aprendiz asignado...");

        Optional<Usuario> aprendizOpt = usuarioRepository.findById(idAprendiz);

        if (aprendizOpt.isEmpty()) {
            System.out.println("❌ Aprendiz no encontrado.");
            return false;
        }

        Usuario aprendiz = aprendizOpt.get();

        if (aprendiz.getFicha() == null) {
            System.out.println("❌ El aprendiz no tiene ficha asignada.");
            return false;
        }

        Integer fichaId = aprendiz.getFicha().getId();

        return fichaRepository.existsByFichaIdAndInstructorId(fichaId, idInstructor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ficha> obtenerFichasActivasPorInstructor(Integer idInstructor) {
        System.out.println("📌 Obtener fichas activas por instructor: " + idInstructor);

        try {
            return fichaRepository.findByInstructorIdWithRelations(idInstructor);
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarAprendicesPorFicha(Integer fichaId) {
        try {
            return fichaRepository.countAprendicesByFichaId(fichaId);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNumeroFicha(String numeroFicha) {
        try {
            return fichaRepository.existsByNumeroFicha(numeroFicha);
        } catch (Exception e) {
            return false;
        }
    }
}
