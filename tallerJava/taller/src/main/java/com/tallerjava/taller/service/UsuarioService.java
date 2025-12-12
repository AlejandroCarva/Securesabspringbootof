package com.tallerjava.taller.service;

import com.tallerjava.taller.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> buscarPorId(Integer id);
    Optional<Usuario> buscarPorCorreo(String correo);
    Usuario guardar(Usuario usuario);
    void eliminar(Integer id);
}
