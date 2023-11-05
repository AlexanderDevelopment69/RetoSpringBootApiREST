package com.bancocomercio.service;

import com.bancocomercio.model.Post;
import com.bancocomercio.model.Usuario;
import com.bancocomercio.repository.PostRepository;
import com.bancocomercio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Post crearPost(Post post, Long usuarioId) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            post.setUsuario(usuario);
            post.setFechaPublicacion(new Date());
            post.setFechaModificacion(null);
            return postRepository.save(post);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    public List<Post> listarPosts() {
        return postRepository.findAll();
    }

    public List<Post> listarPostsPorUsuario(Long usuarioId) {
        return postRepository.findAllByUsuarioId(usuarioId);
    }


    public Post modificarPost(Long postId, Post post, Long usuarioId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            if (existingPost.getUsuario().getId().equals(usuarioId)) {
                existingPost.setText(post.getText());
                existingPost.setFechaModificacion(new Date());
                return postRepository.save(existingPost);
            } else {
                throw new RuntimeException("No tienes permiso para modificar este post");
            }
        } else {
            throw new RuntimeException("Post no encontrado");
        }
    }


    public void eliminarPost(Long postId, Long usuarioId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            // Verifica si el usuario que intenta eliminar es el mismo que lo creó
            if (existingPost.getUsuario().getId().equals(usuarioId)) {
                postRepository.delete(existingPost);
            } else {
                throw new RuntimeException("No tienes permiso para eliminar este post");
            }
        } else {
            throw new RuntimeException("Post no encontrado");
        }
    }

}
