package com.bancocomercio.service;

import com.bancocomercio.model.Usuario;
import com.bancocomercio.repository.UsuarioRepository;
import com.bancocomercio.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCrearUsuario() {
        // Crear un usuario de ejemplo
        Usuario usuario = new Usuario();
        usuario.setName("John");
        usuario.setLastName("Doe");
        usuario.setCellphone("123456789");
        usuario.setPassword("password");

        // Simular el comportamiento del repositorio
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Llamar al método del servicio
        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);

        // Verificar que el nuevo usuario se haya creado correctamente
        assertEquals("John", nuevoUsuario.getName());
        assertEquals("Doe", nuevoUsuario.getLastName());
        assertEquals("123456789", nuevoUsuario.getCellphone());
        assertEquals("password", nuevoUsuario.getPassword());
        // Asegurarse de que se establezca la fecha de alta
        assertNotNull(nuevoUsuario.getFechaAlta());
    }

    private void assertNotNull(Date fechaAlta) {
    }

    @Test
    public void testListarUsuarios() {
        // Crea una lista de usuarios de ejemplo
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setName("John");
        usuario1.setLastName("Doe");
        usuario1.setCellphone("123456789");
        usuario1.setPassword("password");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setName("Jane");
        usuario2.setLastName("Smith");
        usuario2.setCellphone("987654321");
        usuario2.setPassword("newpassword");

        List<Usuario> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(usuario1);
        listaUsuarios.add(usuario2);

        // Simula el comportamiento del repositorio para devolver la lista de usuarios
        when(usuarioRepository.findAll()).thenReturn(listaUsuarios);

        // Llama al método del servicio
        List<Usuario> usuariosEncontrados = usuarioService.listarUsuarios();

        // Verifica que la lista de usuarios devuelta coincide con la lista de ejemplo
        assertEquals(listaUsuarios.size(), usuariosEncontrados.size());

        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario usuarioEsperado = listaUsuarios.get(i);
            Usuario usuarioEncontrado = usuariosEncontrados.get(i);

            assertEquals(usuarioEsperado.getId(), usuarioEncontrado.getId());
            assertEquals(usuarioEsperado.getName(), usuarioEncontrado.getName());
            assertEquals(usuarioEsperado.getLastName(), usuarioEncontrado.getLastName());
            assertEquals(usuarioEsperado.getCellphone(), usuarioEncontrado.getCellphone());
            assertEquals(usuarioEsperado.getPassword(), usuarioEncontrado.getPassword());
        }
    }

    @Test
    public void testModificarUsuario() {
        // Crea un usuario de ejemplo
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setName("John");
        usuario.setLastName("Doe");
        usuario.setCellphone("123456789");
        usuario.setPassword("password");

        // Escenario de éxito: Usuario encontrado y modificado
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioModificado = new Usuario();
        usuarioModificado.setId(1L);
        usuarioModificado.setName("Jane");
        usuarioModificado.setLastName("Smith");
        usuarioModificado.setCellphone("987654321");
        usuarioModificado.setPassword("newpassword");

        Usuario resultado = usuarioService.modificarUsuario(1L, usuarioModificado);

        assertEquals("Jane", resultado.getName());
        assertEquals("Smith", resultado.getLastName());
        assertEquals("987654321", resultado.getCellphone());
        assertEquals("newpassword", resultado.getPassword());

        // Escenario de fracaso: Usuario no encontrado
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.modificarUsuario(2L, usuarioModificado));
    }

    @Test
    public void testEliminarUsuario() {
        // Escenario de éxito: Usuario encontrado y eliminado
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(1L));

        // Escenario de fracaso: Usuario no encontrado
        when(usuarioRepository.existsById(2L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioService.eliminarUsuario(2L));
        assertEquals("Usuario no encontrado", exception.getMessage());
    }



}
