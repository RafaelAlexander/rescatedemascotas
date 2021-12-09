package ar.edu.utn.frba.dds.test;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsuarioTest {
  @Test
  public void usuarioConPasswordInvalida() {

    RuntimeException UsuarioInvalido = assertThrows(RuntimeException.class, () -> {
      new Usuario("LaPulga10", "a1");
    });

    assertEquals("La contraseña debe tener al menos 8 caracteres", UsuarioInvalido.getMessage());
  }

  @Test
  public void usuarioConPasswordInvalidaPorSerCorta() {

    RuntimeException usuarioInvalido = assertThrows(RuntimeException.class, () -> {
      new Usuario("LaPulga10", "a1");
    });

    assertEquals("La contraseña debe tener al menos 8 caracteres", usuarioInvalido.getMessage());
  }


  @Test
  public void usuarioConPasswordInvalidaPorContenerRepeticionDeCaracteres() {

    RuntimeException usuarioInvalido = assertThrows(RuntimeException.class, () -> {
      new Usuario("Cris.R7", "siuuuuuuuuuuuuuuuuuuu");
    });

    assertEquals("La contraseña contiene una repetición de caracteres", usuarioInvalido.getMessage());
  }

  @Test
  public void crearUsuarioExitoso() throws ClassNotFoundException {
    assertEquals(new Usuario("RafaBenitez", "yE^hy3m!e*H#").getUsername(), "RafaBenitez");
  }
}
