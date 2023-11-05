package com.bancocomercio.service;

import com.bancocomercio.model.Usuario;
import com.bancocomercio.model.Post;
import com.bancocomercio.repository.UsuarioRepository;
import com.bancocomercio.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UsuarioPostIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testCrearPublicacionParaUsuario() {
        // Crea un usuario
        Usuario usuario = new Usuario();
        usuario.setCellphone("1234567890");
        usuario.setName("Ejemplo");
        usuario.setLastName("Usuario");

        // Guarda el usuario en la base de datos
        usuarioRepository.save(usuario);

        // Crear una publicación para el usuario
        Post post = new Post();
        post.setText("Contenido de la publicación");
        post.setUsuario(usuario);

        // Guarda la publicación en la base de datos
        postRepository.save(post);

        // Recupera todas las publicaciones del usuario
        List<Post> publicacionesDelUsuario = postRepository.findAllByUsuarioId(usuario.getId());

        // Verifica que la publicación se haya creado correctamente
        assert publicacionesDelUsuario.size() == 1;
        assert publicacionesDelUsuario.get(0).getText().equals("Contenido de la publicación");
    }

    @Test
    public void testModificarPublicacionPorUsuario() {
        // Recupera un usuario y una publicación de la base de datos (asegúrate de que existan)
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(1L);
        Optional<Post> postOptional = postRepository.findById(1L);

        if (usuarioOptional.isPresent() && postOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            Post post = postOptional.get();

            // Modifica el contenido de la publicación
            post.setText("Contenido modificado");
            postRepository.save(post);

            // Recupera la publicación modificada y verificar que el texto haya cambiado
            Post publicacionModificada = postRepository.findById(post.getId()).orElse(null);
            assert publicacionModificada != null;
            assert publicacionModificada.getText().equals("Contenido modificado");
        }
    }

    @Test
    public void testListarPublicacionPorUsuario() {
        // Recupera un usuario de la base de datos (asegúrate de que exista)
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(1L);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Recupera todas las publicaciones del usuario
            List<Post> publicacionesDelUsuario = postRepository.findAllByUsuarioId(usuario.getId());

            // Verifica que la lista de publicaciones no esté vacía
            assert !publicacionesDelUsuario.isEmpty();
        }
    }

    @Test
    public void testEliminarPublicacionPorUsuario() {
        // Recupera un usuario y una publicación de la base de datos (asegúrate de que existan)
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(1L);
        Optional<Post> postOptional = postRepository.findById(1L);

        if (usuarioOptional.isPresent() && postOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            Post post = postOptional.get();

            // Elimina la publicación
            postRepository.delete(post);

            // Verifica que la publicación haya sido eliminada
            Post publicacionEliminada = postRepository.findById(post.getId()).orElse(null);
            assert publicacionEliminada == null;
        }
    }



}
