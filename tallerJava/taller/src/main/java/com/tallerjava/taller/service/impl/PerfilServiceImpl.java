package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.repository.IUsuarioRepository;
import com.tallerjava.taller.service.IPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilServiceImpl implements IPerfilService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Usuario obtenerPerfil(Integer idUsuario) {
        try {
            System.out.println("? PERFIL SERVICE - Obteniendo perfil para usuario ID: " + idUsuario);
            
            // ✅ CORREGIDO: Usar Integer directamente
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
            
            System.out.println("? Perfil encontrado: " + usuario.getNombre() + " " + usuario.getApellido());
            return usuario;
            
        } catch (Exception e) {
            System.err.println("? ERROR al obtener perfil: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el perfil: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario actualizarPerfil(Usuario usuario) {
        try {
            System.out.println("? PERFIL SERVICE - Actualizando perfil para usuario ID: " + usuario.getIdUsuario());
            
            // Validar que el usuario existe
            if (!usuarioRepository.existsById(usuario.getIdUsuario())) {
                throw new RuntimeException("Usuario no encontrado con ID: " + usuario.getIdUsuario());
            }
            
            // Guardar los cambios
            Usuario usuarioActualizado = usuarioRepository.save(usuario);
            System.out.println("? Perfil actualizado correctamente");
            
            return usuarioActualizado;
            
        } catch (Exception e) {
            System.err.println("? ERROR al actualizar perfil: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el perfil: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarFotoPerfil(Integer idUsuario, String nombreArchivo) {
        try {
            System.out.println("? PERFIL SERVICE - Actualizando foto de perfil para usuario ID: " + idUsuario);
            
            // Obtener el usuario
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
            
            // Actualizar la foto de perfil
            usuario.setFotoPerfil(nombreArchivo);
            usuarioRepository.save(usuario);
            
            System.out.println("? Foto de perfil actualizada: " + nombreArchivo);
            
        } catch (Exception e) {
            System.err.println("? ERROR al actualizar foto de perfil: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar la foto de perfil: " + e.getMessage(), e);
        }
    }
}