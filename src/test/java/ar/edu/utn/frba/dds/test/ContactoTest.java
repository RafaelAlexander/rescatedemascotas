package ar.edu.utn.frba.dds.test;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContactoTest {


  @Test
  public void ContactoSinDatosNoPuedeSerCreada() {

    RuntimeException contactoSinDatos = assertThrows(RuntimeException.class, () -> {
      Contacto contactoEjemplo = contactoNuevo(null, null, null);
    });

    assertEquals("Todos los campos del contacto deben estar rellenados", contactoSinDatos.getMessage());
  }

  public Contacto contactoNuevo(String nombreApellido, String telefono, String email) {
    return new Contacto(nombreApellido, telefono, email);
  }
}
