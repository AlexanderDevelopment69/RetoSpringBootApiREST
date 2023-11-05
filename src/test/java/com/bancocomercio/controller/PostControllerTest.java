package com.bancocomercio.controller;

import com.bancocomercio.model.Post;
import com.bancocomercio.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearPostExitoso() {
        Long usuarioId = 1L;
        Post nuevoPost = new Post();
        when(postService.crearPost(nuevoPost, usuarioId)).thenReturn(nuevoPost);

        ResponseEntity<Object> response = postController.crearPost(usuarioId, nuevoPost);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(nuevoPost, response.getBody());
    }

    @Test
    public void testCrearPostFallo() {
        Long usuarioId = 1L;
        Post nuevoPost = new Post();
        when(postService.crearPost(nuevoPost, usuarioId)).thenReturn(null);

        ResponseEntity<Object> response = postController.crearPost(usuarioId, nuevoPost);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCrearPostUsuarioNoEncontrado() {
        Long usuarioId = 1L;
        Post nuevoPost = new Post();
        when(postService.crearPost(nuevoPost, usuarioId)).thenThrow(new RuntimeException("Usuario no encontrado"));

        ResponseEntity<Object> response = postController.crearPost(usuarioId, nuevoPost);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testListarPostsExitoso() {
        List<Post> posts = List.of(new Post(), new Post());
        when(postService.listarPosts()).thenReturn(posts);

        ResponseEntity<List<Post>>response = postController.listarPosts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts, response.getBody());
    }

    @Test
    public void testListarPostsPorUsuarioExitoso() {
        Long usuarioId = 1L;
        List<Post> posts = List.of(new Post(), new Post());
        when(postService.listarPostsPorUsuario(usuarioId)).thenReturn(posts);

        ResponseEntity<List<Post>> response = postController.listarPostsPorUsuario(usuarioId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts, response.getBody());
    }

    @Test
    public void testModificarPostExitoso() {
        Long postId = 1L;
        Long usuarioId = 1L;
        Post postModificado = new Post();
        when(postService.modificarPost(postId, postModificado, usuarioId)).thenReturn(postModificado);

        ResponseEntity<Object> response = postController.modificarPost(postId, usuarioId, postModificado);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postModificado, response.getBody());
    }

    @Test
    public void testModificarPostFallo() {
        Long postId = 1L;
        Long usuarioId = 1L;
        Post postModificado = new Post();
        when(postService.modificarPost(postId, postModificado, usuarioId)).thenReturn(null);

        ResponseEntity<Object> response = postController.modificarPost(postId, usuarioId, postModificado);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testModificarPostPermisoDenegado() {
        Long postId = 1L;
        Long usuarioId = 1L;
        Post postModificado = new Post();
        when(postService.modificarPost(postId, postModificado, usuarioId)).thenThrow(new RuntimeException("No tienes permiso para modificar este post"));

        ResponseEntity<Object> response = postController.modificarPost(postId, usuarioId, postModificado);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testModificarPostNoEncontrado() {
        Long postId = 1L;
        Long usuarioId = 1L;
        Post postModificado = new Post();
        when(postService.modificarPost(postId, postModificado, usuarioId)).thenThrow(new RuntimeException("Post no encontrado"));

        ResponseEntity<Object> response = postController.modificarPost(postId, usuarioId, postModificado);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testEliminarPostExitoso() {
        Long postId = 1L;
        Long usuarioId = 1L;
        ResponseEntity<Object> response = postController.eliminarPost(postId, usuarioId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post eliminado con éxito", response.getBody());
    }

    @Test
    public void testEliminarPostPermisoDenegado() {
        Long postId = 1L;
        Long usuarioId = 1L;
        Mockito.doThrow(new RuntimeException("No tienes permiso para eliminar este post")).when(postService).eliminarPost(postId, usuarioId);

        ResponseEntity<Object> response = postController.eliminarPost(postId, usuarioId);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testEliminarPostNoEncontrado() {
        Long postId = 1L;
        Long usuarioId = 1L;
        Mockito.doThrow(new RuntimeException("Post no encontrado")).when(postService).eliminarPost(postId, usuarioId);

        ResponseEntity<Object> response = postController.eliminarPost(postId, usuarioId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
