package com.bancocomercio.controller;

import com.bancocomercio.model.Usuario;
import com.bancocomercio.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearUsuarioExitoso() {
        Usuario nuevoUsuario = new Usuario();
        when(usuarioService.crearUsuario(nuevoUsuario)).thenReturn(nuevoUsuario);

        ResponseEntity<Usuario> response = usuarioController.crearUsuario(nuevoUsuario);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(nuevoUsuario, response.getBody());
    }

    @Test
    public void testCrearUsuarioFallo() {
        Usuario nuevoUsuario = new Usuario();
        when(usuarioService.crearUsuario(nuevoUsuario)).thenReturn(null);

        ResponseEntity<Usuario> response = usuarioController.crearUsuario(nuevoUsuario);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testListarUsuariosExitoso() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        when(usuarioService.listarUsuarios()).thenReturn(usuarios);

        ResponseEntity<List<Usuario>> response = usuarioController.listarUsuarios();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarios, response.getBody());
    }


    @Test
    public void testListarUsuariosFallo() {
        // Configurar el comportamiento del servicio mock
        when(usuarioService.listarUsuarios()).thenThrow(new RuntimeException("Error al listar usuarios"));

        // Llamar al método de controlador que deseas probar
        ResponseEntity<List<Usuario>> response = usuarioController.listarUsuarios();

        // Verificar el resultado
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testModificarUsuarioExitoso() {
        Long usuarioId = 1L;
        Usuario usuarioModificado = new Usuario();
        when(usuarioService.modificarUsuario(usuarioId, usuarioModificado)).thenReturn(usuarioModificado);

        ResponseEntity<Object> response = usuarioController.modificarUsuario(usuarioId, usuarioModificado);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioModificado, response.getBody());
    }

    @Test
    public void testModificarUsuarioFallo() {
        Long usuarioId = 1L;
        Usuario usuarioModificado = new Usuario();
        when(usuarioService.modificarUsuario(usuarioId, usuarioModificado)).thenReturn(null);

        ResponseEntity<Object> response = usuarioController.modificarUsuario(usuarioId, usuarioModificado);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testModificarUsuarioNoEncontrado() {
        Long usuarioId = 1L;
        Usuario usuarioModificado = new Usuario();
        when(usuarioService.modificarUsuario(usuarioId, usuarioModificado)).thenThrow(new RuntimeException("Usuario no encontrado"));

        ResponseEntity<Object> response = usuarioController.modificarUsuario(usuarioId, usuarioModificado);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testEliminarUsuarioExitoso() {
        Long usuarioId = 1L;
        ResponseEntity<String> response = usuarioController.eliminarUsuario(usuarioId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario eliminado con éxito", response.getBody());
    }

    @Test
    public void testEliminarUsuarioFallo() {
        Long usuarioId = 1L;
        Mockito.doThrow(new RuntimeException("Usuario no encontrado")).when(usuarioService).eliminarUsuario(usuarioId);

        ResponseEntity<String> response = usuarioController.eliminarUsuario(usuarioId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
