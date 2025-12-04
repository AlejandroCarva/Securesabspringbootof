package com.tallerjava.taller.service.impl;

import com.tallerjava.taller.dto.ActualizarAprendizDTO;
import com.tallerjava.taller.model.Usuario;
import com.tallerjava.taller.repository.IUsuarioRepository;
import com.tallerjava.taller.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    
    @Autowired
    private IUsuarioRepository usuarioRepository;
    
    @Autowired
    private Validator validator; // Para validar el DTO
    
    private final String UPLOAD_DIR = "uploads/";

    @Override
    public Usuario obtenerPorId(Long id) {
        System.out.println("? SERVICE: Obtener usuario por ID (Long): " + id);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id.intValue());
        return usuarioOpt.orElse(null);
    }

    @Override
    public Usuario obtenerPorId(Integer id) {
        System.out.println("? SERVICE: Obtener usuario por ID (Integer): " + id);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        return usuarioOpt.orElse(null);
    }

    @Override
    public Usuario obtenerPorCedula(String cedula) {
        System.out.println("? SERVICE: Obtener usuario por cédula: " + cedula);
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCedula(cedula);
        return usuarioOpt.orElse(null);
    }

    @Override
    public List<Usuario> obtenerAprendicesPorFicha(Integer fichaId) {
        System.out.println("? SERVICE: Obtener aprendices por ficha ID: " + fichaId);
        
        try {
            List<Usuario> aprendices = usuarioRepository.findByFichaId(fichaId);
            System.out.println("? Aprendices encontrados: " + aprendices.size());
            return aprendices;
            
        } catch (Exception e) {
            System.err.println("? ERROR al obtener aprendices por ficha: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Retorna lista vacía en caso de error
        }
    }

    @Override
    public Long contarAprendicesPorFicha(Integer fichaId) {
        System.out.println("? SERVICE: Contar aprendices por ficha ID: " + fichaId);
        
        try {
            Long count = usuarioRepository.countByFichaId(fichaId);
            System.out.println("? Count obtenido: " + count);
            return count != null ? count : 0L;
            
        } catch (Exception e) {
            System.err.println("? ERROR al contar aprendices por ficha: " + e.getMessage());
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    @Transactional
    public void actualizarAprendiz(Integer idAprendiz, ActualizarAprendizDTO dto) {
        System.out.println("? SERVICE: Actualizar aprendiz ID: " + idAprendiz);
        System.out.println("? Datos recibidos:");
        System.out.println("? - Cédula: " + dto.getCedula());
        System.out.println("? - Nombre: " + dto.getNombre());
        System.out.println("? - Apellido: " + dto.getApellido());
        System.out.println("? - Correo: " + dto.getCorreo());
        System.out.println("? - Teléfono: " + dto.getTelefono());
        
        try {
            // Validar el DTO
            Set<ConstraintViolation<ActualizarAprendizDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<ActualizarAprendizDTO> violation : violations) {
                    sb.append(violation.getMessage()).append("; ");
                }
                throw new RuntimeException("Datos inválidos: " + sb.toString());
            }
            
            Usuario aprendiz = obtenerPorId(idAprendiz);
            if (aprendiz == null) {
                throw new RuntimeException("Aprendiz con ID " + idAprendiz + " no encontrado");
            }
            
            // Verificar si la cédula ya existe (si cambió)
            if (!aprendiz.getCedula().equals(dto.getCedula())) {
                Usuario existenteConCedula = obtenerPorCedula(dto.getCedula());
                if (existenteConCedula != null && !existenteConCedula.getIdUsuario().equals(idAprendiz)) {
                    throw new RuntimeException("La cédula " + dto.getCedula() + " ya está registrada");
                }
            }
            
            // Verificar si el correo ya existe (si cambió)
            if (!aprendiz.getCorreo().equals(dto.getCorreo())) {
                Optional<Usuario> existenteConCorreo = usuarioRepository.findByCorreo(dto.getCorreo());
                if (existenteConCorreo.isPresent() && 
                    !existenteConCorreo.get().getIdUsuario().equals(idAprendiz)) {
                    throw new RuntimeException("El correo " + dto.getCorreo() + " ya está registrado");
                }
            }
            
            // Actualizar datos
            aprendiz.setCedula(dto.getCedula());
            aprendiz.setNombre(dto.getNombre());
            aprendiz.setApellido(dto.getApellido());
            aprendiz.setCorreo(dto.getCorreo());
            
            if (dto.getTelefono() != null && !dto.getTelefono().isEmpty()) {
                aprendiz.setTelefono(dto.getTelefono());
            }
            
            // Guardar cambios
            usuarioRepository.save(aprendiz);
            System.out.println("? ✅ Aprendiz actualizado exitosamente");
            
        } catch (Exception e) {
            System.err.println("? ❌ ERROR al actualizar aprendiz: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-lanzar para que el controller lo capture
        }
    }

    @Override
    @Transactional
    public void actualizarFotoPerfil(Integer idUsuario, MultipartFile archivo) {
        System.out.println("? SERVICE: Actualizar foto perfil (Integer): " + idUsuario);
        actualizarFotoPerfilImpl(idUsuario.longValue(), archivo);
    }

    @Override
    @Transactional
    public void actualizarFotoPerfil(Long idUsuario, MultipartFile archivo) {
        System.out.println("? SERVICE: Actualizar foto perfil (Long): " + idUsuario);
        actualizarFotoPerfilImpl(idUsuario, archivo);
    }
    
    private void actualizarFotoPerfilImpl(Long idUsuario, MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                throw new RuntimeException("El archivo está vacío");
            }
            
            // Validar tipo de archivo
            String contentType = archivo.getContentType();
            if (contentType == null || 
                (!contentType.equals("image/jpeg") && 
                 !contentType.equals("image/png") && 
                 !contentType.equals("image/gif") &&
                 !contentType.equals("image/jpg"))) {
                throw new RuntimeException("Solo se permiten imágenes (JPEG, JPG, PNG, GIF)");
            }
            
            // Validar tamaño (máximo 5MB)
            if (archivo.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("La imagen no debe superar los 5MB");
            }
            
            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("? Directorio creado: " + uploadPath.toAbsolutePath());
            }
            
            // Generar nombre único para el archivo
            String originalFilename = archivo.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String nuevoNombre = "perfil_" + idUsuario + "_" + System.currentTimeMillis() + extension;
            Path filePath = uploadPath.resolve(nuevoNombre);
            
            // Guardar archivo
            Files.copy(archivo.getInputStream(), filePath);
            System.out.println("? Archivo guardado: " + filePath.toAbsolutePath());
            
            // Obtener usuario y actualizar nombre de foto
            Usuario usuario = obtenerPorId(idUsuario);
            if (usuario != null) {
                // Eliminar foto anterior si existe
                if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) {
                    try {
                        Path oldFilePath = uploadPath.resolve(usuario.getFotoPerfil());
                        Files.deleteIfExists(oldFilePath);
                        System.out.println("? Foto anterior eliminada: " + usuario.getFotoPerfil());
                    } catch (IOException e) {
                        System.err.println("? Advertencia: No se pudo eliminar la foto anterior: " + e.getMessage());
                    }
                }
                
                usuario.setFotoPerfil(nuevoNombre);
                usuarioRepository.save(usuario);
                System.out.println("? ✅ Foto de perfil actualizada exitosamente para usuario: " + usuario.getNombre());
            } else {
                // Si no existe usuario, eliminar la foto recién guardada
                Files.deleteIfExists(filePath);
                throw new RuntimeException("Usuario con ID " + idUsuario + " no encontrado");
            }
            
        } catch (IOException e) {
            System.err.println("? ❌ ERROR de IO al guardar foto: " + e.getMessage());
            throw new RuntimeException("Error al guardar la foto: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("? ❌ ERROR al actualizar foto perfil: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean esInstructor(Integer idUsuario) {
        System.out.println("? SERVICE: Verificar si usuario es instructor ID: " + idUsuario);
        
        try {
            // Buscar usuarios con rol de instructor
            List<Usuario> instructores = usuarioRepository.findByRolName("instructor");
            
            boolean esInstructor = instructores.stream()
                .anyMatch(u -> u.getIdUsuario().equals(idUsuario));
            
            System.out.println("? Es instructor: " + esInstructor);
            return esInstructor;
            
        } catch (Exception e) {
            System.err.println("? ERROR al verificar rol instructor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean esAprendiz(Integer idUsuario) {
        System.out.println("? SERVICE: Verificar si usuario es aprendiz ID: " + idUsuario);
        
        try {
            boolean esAprendiz = usuarioRepository.isAprendiz(idUsuario);
            System.out.println("? Es aprendiz: " + esAprendiz);
            return esAprendiz;
            
        } catch (Exception e) {
            System.err.println("? ERROR al verificar rol aprendiz: " + e.getMessage());
            return false;
        }
    }
}