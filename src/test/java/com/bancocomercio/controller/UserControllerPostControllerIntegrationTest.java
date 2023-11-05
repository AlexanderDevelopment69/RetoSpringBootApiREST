package com.bancocomercio.controller;

import com.bancocomercio.model.Post;
import com.bancocomercio.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserControllerPostControllerIntegrationTest {

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private PostController postController;

    @Test
    public void testCrearUsuarioYPost() {
        // Crear un usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setName("Ejemplo");
        nuevoUsuario.setLastName("Usuario");
        ResponseEntity<Usuario> usuarioResponse = usuarioController.crearUsuario(nuevoUsuario);

        // Verifica que se creó el usuario con éxito
        assertEquals(HttpStatus.CREATED, usuarioResponse.getStatusCode());
        assertNotNull(usuarioResponse.getBody());

        // Crea una publicación asociada a ese usuario
        Post nuevaPublicacion = new Post();
        nuevaPublicacion.setText("Este es un ejemplo de publicación.");
        ResponseEntity<Object> postResponse = postController.crearPost(usuarioResponse.getBody().getId(), nuevaPublicacion);

        // Verifica que se creó la publicación con éxito
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
    }

    @Test
    public void testListarPublicacionPorUsuario() {
        // Crea un usuario y una publicación
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setName("Ejemplo");
        nuevoUsuario.setLastName("Usuario");
        ResponseEntity<Usuario> usuarioResponse = usuarioController.crearUsuario(nuevoUsuario);
        assertNotNull(usuarioResponse.getBody());

        Post nuevaPublicacion = new Post();
        nuevaPublicacion.setText("Este es un ejemplo de publicación.");
        ResponseEntity<Object> postResponse = postController.crearPost(usuarioResponse.getBody().getId(), nuevaPublicacion);
        assertNotNull(postResponse.getBody());

        // Lista las publicaciones del usuario
        ResponseEntity<List<Post>> listarPostsResponse = postController.listarPostsPorUsuario(usuarioResponse.getBody().getId());

        // Verifica que se obtuvo una lista de publicaciones
        assertEquals(HttpStatus.OK, listarPostsResponse.getStatusCode());
        assertNotNull(listarPostsResponse.getBody());
        assertEquals(1, listarPostsResponse.getBody().size()); // Debería haber una publicación en la lista
    }


    @Test
    public void testModificarPublicacionPorUsuario() {
        // Crea un usuario y una publicación
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setName("Ejemplo");
        nuevoUsuario.setLastName("Usuario");
        ResponseEntity<Usuario> usuarioResponse = usuarioController.crearUsuario(nuevoUsuario);
        assertNotNull(usuarioResponse.getBody());

        Post nuevaPublicacion = new Post();
        nuevaPublicacion.setText("Este es un ejemplo de publicación.");
        ResponseEntity<Object> postResponse = postController.crearPost(usuarioResponse.getBody().getId(), nuevaPublicacion);
        assertNotNull(postResponse.getBody());

        // Obtiene el ID de la publicación
        Long postId = null;
        if (postResponse.getBody() instanceof Post) {
            postId = ((Post) postResponse.getBody()).getId();
        }

        // Asegura de que se haya obtenido un ID válido
        assertNotNull(postId);

        // Modifica la publicación
        nuevaPublicacion.setText("Texto modificado de la publicación.");
        ResponseEntity<Object> modificacionResponse = postController.modificarPost(postId, usuarioResponse.getBody().getId(), nuevaPublicacion);

        // Verifica que la publicación se modificó con éxito
        assertEquals(HttpStatus.OK, modificacionResponse.getStatusCode());
    }




    @Test
    public void testEliminarPublicacionPorUsuario() {
        // Crea un usuario y una publicación
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setName("Ejemplo");
        nuevoUsuario.setLastName("Usuario");
        ResponseEntity<Usuario> usuarioResponse = usuarioController.crearUsuario(nuevoUsuario);
        assertNotNull(usuarioResponse.getBody());

        Post nuevaPublicacion = new Post();
        nuevaPublicacion.setText("Este es un ejemplo de publicación.");
        ResponseEntity<Object> postResponse = postController.crearPost(usuarioResponse.getBody().getId(), nuevaPublicacion);
        assertNotNull(postResponse.getBody());

        // Obtiene el ID de la publicación
        Long postId = null;
        if (postResponse.getBody() instanceof Post) {
            postId = ((Post) postResponse.getBody()).getId();
        }

        // Asegura  que se haya obtenido un ID válido
        assertNotNull(postId);

        // Elimina la publicación
        ResponseEntity<Object> eliminacionResponse = postController.eliminarPost(postId, usuarioResponse.getBody().getId());

        // Verifica que la publicación se eliminó con éxito
        assertEquals(HttpStatus.OK, eliminacionResponse.getStatusCode());
    }



}
