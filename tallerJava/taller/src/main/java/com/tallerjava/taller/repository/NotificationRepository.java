package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Contar NO leídas por usuario
    @Query("""
        SELECT COUNT(n)
        FROM Notification n
        WHERE n.usuario.idUsuario = :usuarioId
          AND (n.readFlag = false OR n.readFlag IS NULL)
        """)
    long countUnreadByUsuarioId(@Param("usuarioId") Integer usuarioId);

    // Listar últimas notificaciones por usuario (paginado)
    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.usuario.idUsuario = :usuarioId
        ORDER BY n.createdAt DESC
        """)
    List<Notification> findLatestByUsuario(
            @Param("usuarioId") Integer usuarioId,
            Pageable pageable
    );

    // Traer solo las no leídas para marcar todas como leídas
    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.usuario.idUsuario = :usuarioId
          AND (n.readFlag = false OR n.readFlag IS NULL)
        ORDER BY n.createdAt DESC
        """)
    List<Notification> findUnreadByUsuario(@Param("usuarioId") Integer usuarioId);
}
