package com.bancocomercio.controller;

import com.bancocomercio.model.Post;
import com.bancocomercio.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/crear/{usuarioId}")
    public ResponseEntity<Object> crearPost(@PathVariable Long usuarioId, @RequestBody Post post) {
        try {
            Post nuevoPost = postService.crearPost(post, usuarioId);
            if (nuevoPost != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPost);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el post");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }


    @GetMapping("/listar")
    public ResponseEntity<List<Post>> listarPosts() {
        List<Post> posts = postService.listarPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/listar/{usuarioId}")
    public ResponseEntity<List<Post>> listarPostsPorUsuario(@PathVariable Long usuarioId) {
        List<Post> posts = postService.listarPostsPorUsuario(usuarioId);
        return ResponseEntity.ok(posts);
    }


    @PutMapping("/modificar/{postId}/{usuarioId}")
    public ResponseEntity<Object> modificarPost(
            @PathVariable Long postId,
            @PathVariable Long usuarioId,
            @RequestBody Post post
    ) {
        try {
            Post result = postService.modificarPost(postId, post, usuarioId);
            if (result != null) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        } catch (RuntimeException e) {
            if (e.getMessage().equals("No tienes permiso para modificar este post")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            } else if (e.getMessage().equals("Post no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al modificar el post");
    }


    @DeleteMapping("/eliminar/{postId}/{usuarioId}")
    public ResponseEntity<Object> eliminarPost(@PathVariable Long postId, @PathVariable Long usuarioId) {
        try {
            postService.eliminarPost(postId, usuarioId);
            return ResponseEntity.status(HttpStatus.OK).body("Post eliminado con éxito");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("No tienes permiso para eliminar este post")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            } else if (e.getMessage().equals("Post no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el post");
            }
        }
    }


}
