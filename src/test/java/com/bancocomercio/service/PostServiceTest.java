package com.bancocomercio.service;

import com.bancocomercio.model.Post;
import com.bancocomercio.model.Usuario;
import com.bancocomercio.repository.PostRepository;
import com.bancocomercio.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCrearPost() {
        // Crea objetos de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Post post = new Post();
        post.setText("Texto del post");
        post.setUsuario(usuario);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post nuevoPost = postService.crearPost(post, 1L);

        // Verifica que el post se ha creado correctamente
        assertNotNull(nuevoPost);
        assertEquals("Texto del post", nuevoPost.getText());
        assertEquals(usuario, nuevoPost.getUsuario());
        assertNotNull(nuevoPost.getFechaPublicacion());
        assertNull(nuevoPost.getFechaModificacion());
    }

    @Test
    public void testListarPosts() {
        // Crea una lista de publicaciones de prueba
        Post post1 = new Post();
        post1.setId(1L);
        post1.setText("Publicación 1");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setText("Publicación 2");

        List<Post> publicacionesDePrueba = Arrays.asList(post1, post2);

        // Configura el comportamiento del mock del repositorio
        when(postRepository.findAll()).thenReturn(publicacionesDePrueba);

        // Llama al método que se va a probar
        List<Post> resultado = postService.listarPosts();

        // Verifica que se haya devuelto la lista correctamente
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Publicación 1", resultado.get(0).getText());
        assertEquals("Publicación 2", resultado.get(1).getText());
    }


    @Test
    public void testListarPostsPorUsuario() {
        // Crea un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        // Crea una lista de publicaciones de prueba relacionadas con el usuario
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Post post = new Post();
            post.setId((long) i);
            post.setText("Texto del post " + i);
            post.setUsuario(usuario);
            posts.add(post);
        }

        when(postRepository.findAllByUsuarioId(1L)).thenReturn(posts);

        List<Post> listaDePosts = postService.listarPostsPorUsuario(1L);

        // Verifica que se devuelva la lista de publicaciones relacionadas con el usuario
        assertNotNull(listaDePosts);
        assertEquals(3, listaDePosts.size());
        // Verifica que todas las publicaciones en la lista estén relacionadas con el usuario
        for (Post post : listaDePosts) {
            assertEquals(usuario, post.getUsuario());
        }
    }

    @Test
    public void testModificarPost() {
        // Crea un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        // Crea una publicación de prueba
        Post post = new Post();
        post.setId(1L);
        post.setText("Texto original");
        post.setUsuario(usuario);
        post.setFechaPublicacion(new Date()); // Establece la fecha de publicación

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post modifiedPost = invocation.getArgument(0);
            modifiedPost.setFechaModificacion(new Date()); // Simula la actualización de la fecha de modificación
            return modifiedPost;
        });

        Post postModificado = new Post();
        postModificado.setId(1L);
        postModificado.setText("Texto modificado");

        Post resultado = postService.modificarPost(1L, postModificado, 1L);

        assertNotNull(resultado);
        assertEquals("Texto modificado", resultado.getText());
        assertNotNull(resultado.getFechaModificacion()); // Asegura que la fecha de modificación se haya actualizado
    }


    @Test
    public void testEliminarPost() {
        // Crea un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        // Crea una publicación de prueba
        Post post = new Post();
        post.setId(1L);
        post.setText("Texto del post");
        post.setUsuario(usuario);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        assertDoesNotThrow(() -> postService.eliminarPost(1L, 1L));
    }
}
