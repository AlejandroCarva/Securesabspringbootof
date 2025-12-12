package com.tallerjava.taller.config;

import com.tallerjava.taller.model.Role;
import com.tallerjava.taller.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static class AccountEntry {
        String rolName;
        String cedula;
        String nombre;
        String rawPassword;

        AccountEntry(String rolName, String cedula, String nombre, String rawPassword) {
            this.rolName = rolName;
            this.cedula = cedula;
            this.nombre = nombre;
            this.rawPassword = rawPassword;
        }
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Lista basada en la tabla que compartiste
        List<AccountEntry> entries = Arrays.asList(
            new AccountEntry("Vigilante", "1000856999", "Carlos Sánchez", "vigilante123"),
            new AccountEntry("Instructor", "1000856966", "Jose Paez", "instructor123"),
            new AccountEntry("Instructor", "1000856977", "Daniel Diaz", "instructor123"),
            new AccountEntry("Coordinador", "1000856988", "Laura Martínez", "coordinador123"),
            new AccountEntry("Aprendiz", "1000856944", "Juancho Pérez", "aprendiz123"),
            new AccountEntry("Aprendiz", "1000856943", "Juanchito Gomez", "aprendiz123"),
            new AccountEntry("Aprendiz", "1000856942", "Luisa Castro", "aprendiz123"),
            new AccountEntry("Gestor", "1000856993", "María López", "usuario123")
        );

        for (AccountEntry e : entries) {
            usuarioRepository.findByCedula(e.cedula).ifPresentOrElse(usuario -> {
                // Actualizar contraseña
                String hashed = passwordEncoder.encode(e.rawPassword);
                usuario.setPassword(hashed);

                // Asegurar que tenga el role correcto (buscamos por name en lowercase)
                String roleNameToSearch = e.rolName.toLowerCase();
                try {
                    TypedQuery<Role> q = em.createQuery("SELECT r FROM Role r WHERE LOWER(r.name) = :name", Role.class);
                    q.setParameter("name", roleNameToSearch);
                    Role role = q.getSingleResult();

                    if (usuario.getRoles() == null) {
                        usuario.setRoles(new ArrayList<>());
                    }

                    boolean has = usuario.getRoles().stream().anyMatch(r -> r.getId().equals(role.getId()));
                    if (!has) {
                        usuario.getRoles().add(role);
                    }

                    usuarioRepository.save(usuario);
                    System.out.println("[DataInitializer] Usuario actualizado: cedula=" + e.cedula + ", role=" + role.getName());

                } catch (NoResultException ex) {
                    System.out.println("[DataInitializer] Role no encontrado para: " + e.rolName + ". Solo se actualizó la contraseña para cedula=" + e.cedula);
                    usuarioRepository.save(usuario);
                } catch (Exception ex) {
                    System.err.println("[DataInitializer] Error actualizando usuario " + e.cedula + ": " + ex.getMessage());
                }

            }, () -> {
                // Usuario no existe en BD; avisamos para que lo crees manualmente si lo deseas
                System.out.println("[DataInitializer] Usuario con cédula " + e.cedula + " no encontrado en la base de datos.");
            });
        }

        System.out.println("[DataInitializer] Proceso finalizado.");
    }
}

