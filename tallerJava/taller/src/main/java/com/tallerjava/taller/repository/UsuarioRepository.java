package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCedula(String cedula);

    @Query("SELECT u FROM Usuario u WHERE REPLACE(REPLACE(u.cedula, ' ', ''), '-', '') = REPLACE(REPLACE(:cedula, ' ', ''), '-', '')")
    Optional<Usuario> findByCedulaFlexible(@Param("cedula") String cedula);

    // Buscar usuarios por nombre de rol (case insensitive)
    @Query(value = """
        SELECT u.* FROM usuarios u
        INNER JOIN model_has_roles mhr ON u.id_usuario = mhr.model_id
        INNER JOIN roles r ON mhr.role_id = r.id
        WHERE LOWER(r.name) = LOWER(:roleName)
        """, nativeQuery = true)
    Collection<Usuario> findByRoleNameIgnoreCase(@Param("roleName") String roleName);
}
