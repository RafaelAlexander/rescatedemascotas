package ar.edu.utn.frba.dds.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import ar.edu.utn.frba.dds.dominio.Rescate.Rescate;
import org.junit.jupiter.api.Test;
import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.Rescate.ParticipanteRescate;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;

class DatosPersonalesTest {

  @Test
  public void creacionFallidaRescatistaSinNombre() {

    RuntimeException rescatistaSinNombreApellidoException = assertThrows(RuntimeException.class, () -> {
      new DatosPersonales("",
          "Maradona",
          TipoDocumento.DNI,
          "1202121200",
          Collections.singletonList(new Contacto("Diego Maradona",
              "",
              "eldiegote10@gmail.com")),
          LocalDate.now(),
          "Av SiempreViva 123");
    });

    assertEquals("Usted no relleno todos los campos", rescatistaSinNombreApellidoException.getMessage());

  }

  @Test
  public void creacionFallidaRescatistaSinFechaNacimiento() {

    RuntimeException rescatistaSinFechaNacimientoException = assertThrows(RuntimeException.class, () -> {
      new DatosPersonales("Diego",
          "Maradona",
          TipoDocumento.DNI,
          "1202121200",
          Collections.singletonList(new Contacto("Diego Maradona",
              "",
              "eldiegote10@gmail.com")),
          null,
          "Av SiempreViva 123");
    });

    assertEquals("Usted no relleno todos los campos", rescatistaSinFechaNacimientoException.getMessage());
  }

  @Test
  public void creacionFallidaRescatistaSinTipoDocumento() {

    RuntimeException rescatistaSinDocumentoException = assertThrows(RuntimeException.class, () -> {
      new DatosPersonales("Diego",
          "Maradona",
          null,
          "1202121200",
          Collections.singletonList(new Contacto("Diego Maradona",
              "",
              "eldiegote10@gmail.com")),
          LocalDate.now(),
          "Av SiempreViva 123");
    });

    assertEquals("Usted no relleno todos los campos", rescatistaSinDocumentoException.getMessage());

  }

  @Test
  public void creacionFallidaRescatistaSinNroDocumento() {

    RuntimeException rescatistaSinDocumentoException = assertThrows(RuntimeException.class, () -> {
      new DatosPersonales("Diego",
          "Maradona",
          TipoDocumento.DNI,
          "",
          Collections.singletonList(new Contacto("Diego Maradona",
              "",
              "eldiegote10@gmail.com")),
          LocalDate.now(),
          "Av SiempreViva 123");
    });

    assertEquals("Usted no relleno todos los campos", rescatistaSinDocumentoException.getMessage());
  }

  @Test
  public void creacionFallidaRescatistaSinDireccion() {

    RuntimeException rescatistaSinDireccionException = assertThrows(RuntimeException.class, () -> {
      new DatosPersonales("Diego",
          "Maradona",
          TipoDocumento.DNI,
          "1202121200",
          Collections.singletonList(new Contacto("Diego Maradona",
              "",
              "eldiegote10@gmail.com")),
          LocalDate.now(),
          "");
    });

    assertEquals("Usted no relleno todos los campos", rescatistaSinDireccionException.getMessage());
  }

}