package com.tallerjava.taller.service;

import com.tallerjava.taller.dto.ActualizarAprendizDTO;
import com.tallerjava.taller.model.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUsuarioService {
    
    // ✅ MÉTODOS EXISTENTES
    Usuario obtenerPorId(Long id);
    Usuario obtenerPorCedula(String cedula);
    Usuario obtenerPorId(Integer id);
    void actualizarFotoPerfil(Integer idUsuario, MultipartFile archivo);
    
    // ✅ NUEVOS MÉTODOS PARA INSTRUCTOR
    List<Usuario> obtenerAprendicesPorFicha(Integer fichaId);
    Long contarAprendicesPorFicha(Integer fichaId);
    void actualizarAprendiz(Integer idAprendiz, ActualizarAprendizDTO dto);
    void actualizarFotoPerfil(Long idUsuario, MultipartFile archivo);
    
    // Opcional: Para verificar roles
    boolean esInstructor(Integer idUsuario);
    boolean esAprendiz(Integer idUsuario);
}