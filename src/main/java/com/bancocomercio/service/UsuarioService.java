// UsuarioService.java
package com.bancocomercio.service;

import com.bancocomercio.model.Usuario;
import com.bancocomercio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Usuario crearUsuario(Usuario usuario) {
        usuario.setFechaAlta(new Date());
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    public Usuario modificarUsuario(Long id, Usuario usuario) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario existingUsuario = optionalUsuario.get();
            existingUsuario.setCellphone(usuario.getCellphone());
            existingUsuario.setName(usuario.getName());
            existingUsuario.setLastName(usuario.getLastName());
            existingUsuario.setPassword(usuario.getPassword());
            existingUsuario.setFechaModificacion(new Date());
            return usuarioRepository.save(existingUsuario);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }


    public void eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}
