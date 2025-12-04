package com.tallerjava.taller.service;

import com.tallerjava.taller.model.Usuario;

public interface IPerfilService {
    
    /**
     * Obtiene el perfil de un usuario
     * @param idUsuario ID del usuario
     * @return Usuario con información del perfil
     */
    Usuario obtenerPerfil(Integer idUsuario);
    
    /**
     * Actualiza la información del perfil de un usuario
     * @param usuario Usuario con la información actualizada
     * @return Usuario actualizado
     */
    Usuario actualizarPerfil(Usuario usuario);
    
    /**
     * Actualiza la foto de perfil de un usuario
     * @param idUsuario ID del usuario
     * @param nombreArchivo Nombre del archivo de la foto
     */
    void actualizarFotoPerfil(Integer idUsuario, String nombreArchivo);
}