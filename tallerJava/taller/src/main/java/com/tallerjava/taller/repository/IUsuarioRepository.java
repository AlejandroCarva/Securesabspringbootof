package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // ✅ MÉTODOS EXISTENTES (los mantienes)
    Optional<Usuario> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
    Optional<Usuario> findByCorreo(String correo);
    
    // ✅ NUEVOS MÉTODOS PARA INSTRUCTOR:
    
    // 1. Obtener aprendices por ficha
    @Query("SELECT u FROM Usuario u WHERE u.ficha.idFicha = :fichaId")
    List<Usuario> findByFichaId(@Param("fichaId") Integer fichaId);
    
    // 2. Contar aprendices por ficha
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.ficha.idFicha = :fichaId")
    Long countByFichaId(@Param("fichaId") Integer fichaId);
    
    // 3. Verificar si usuario es aprendiz (tiene ficha asignada)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.idUsuario = :id AND u.ficha IS NOT NULL")
    boolean isAprendiz(@Param("id") Integer id);
    
    // 4. Buscar usuarios por rol (para encontrar instructores)
    @Query(value = """
        SELECT u.* FROM usuarios u
        INNER JOIN model_has_roles mhr ON u.id_usuario = mhr.model_id
        INNER JOIN roles r ON mhr.role_id = r.id
        WHERE r.name = :rolName
        """, nativeQuery = true)
    List<Usuario> findByRolName(@Param("rolName") String rolName);
    
    // 5. Buscar usuarios por ficha y rol
    @Query(value = """
        SELECT u.* FROM usuarios u
        INNER JOIN model_has_roles mhr ON u.id_usuario = mhr.model_id
        INNER JOIN roles r ON mhr.role_id = r.id
        WHERE u.id_ficha = :fichaId AND r.name = :rolName
        """, nativeQuery = true)
    List<Usuario> findByFichaIdAndRolName(
        @Param("fichaId") Integer fichaId, 
        @Param("rolName") String rolName);
}