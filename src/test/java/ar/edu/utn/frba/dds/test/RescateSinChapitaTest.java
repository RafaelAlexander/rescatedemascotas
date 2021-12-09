package ar.edu.utn.frba.dds.test;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import ar.edu.utn.frba.dds.dominio.Rescate.ParticipanteRescate;
import ar.edu.utn.frba.dds.dominio.Rescate.RescateSinChapita;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RescateSinChapitaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  public void validarCuandoElRescatistaEsNulo() {

    Asociacion nuevaAsociacion = new Asociacion(1, 1);

    RuntimeException rescateSinChapitaSinRescatista = assertThrows(RuntimeException.class, () -> {
      RescateSinChapita rescate = new RescateSinChapita(null, null);
      nuevaAsociacion.aprobar(rescate);
    });

    assertEquals("El rescatista no tiene los datos esperados", rescateSinChapitaSinRescatista.getMessage());
  }

  @Test
  public void validarCuandoLaMascotaEsNula() {

    Contacto contactoRecatista = new Contacto("Oscar Gonzalez", "11465789", "oscarg@gmail.com");
    List<Contacto> contactosDeRescatista = new ArrayList<>();
    contactosDeRescatista.add(contactoRecatista);
    Asociacion nuevaAsociacion = new Asociacion(1, 1);
    ParticipanteRescate resctista = Fixture.rescatistaGenerico();
    RuntimeException rescateSinChapitaSinRescatista = assertThrows(RuntimeException.class, () -> {
      RescateSinChapita rescate = new RescateSinChapita(resctista, null);
      nuevaAsociacion.aprobar(rescate);
    });
    assertEquals("El animal perdido no tiene los datos esperados", rescateSinChapitaSinRescatista.getMessage());
  }

  @Test
  public void AprobadoSeAgregaAAsociacion() throws SQLException, IOException {

    Contacto contactoRecatista = new Contacto("Oscar Gonzalez", "11465789", "oscarg@gmail.com");
    List<Contacto> contactosDeRescatista = new ArrayList<>();
    contactosDeRescatista.add(contactoRecatista);
    ParticipanteRescate resctista = Fixture.rescatistaGenerico();
    Asociacion nuevaAsociacion = new Asociacion(1, 1);

    //creeamos la mascota
    Foto nuevaFoto = Fixture.fotosGenericas().stream().findFirst().orElse(null);
    List<Foto> listaDeFotos = new ArrayList<>();
    listaDeFotos.add(nuevaFoto);
    AnimalPerdido mascotaPerdida = new AnimalPerdido(null, listaDeFotos, "Mal", 1, 2);

    RescateSinChapita nuevoRescate = new RescateSinChapita(resctista, mascotaPerdida);
    nuevaAsociacion.aprobar(nuevoRescate);

    assertEquals(1, nuevaAsociacion.cantidadPublicacionesAprobadas());
  }


}
