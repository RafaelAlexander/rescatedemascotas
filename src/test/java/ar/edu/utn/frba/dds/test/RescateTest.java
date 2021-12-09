package ar.edu.utn.frba.dds.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Rescate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Fixture.Fixture;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class RescateTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }


  @Test
  public void cargarRescateValido() throws SQLException, IOException {
    Rescate rescate = new Rescate(Fixture.rescatistaGenerico(), Fixture.animalPerdidoGenerico(), LocalDate.of(2021, 5, 4));
    assertEquals(rescate.getAnimalPerdido().getChapita(), Long.valueOf(1));
  }

  @Test
  public void creacionFallidaRescateSinFecha() {

    RuntimeException rescateSinFechaException = assertThrows(RuntimeException.class, () -> {
      Rescate rescate = new Rescate(Fixture.rescatistaGenerico(), Fixture.animalPerdidoGenerico(), null);
    });

    assertEquals("Se debe ingresar la fecha en que se encontro el animal perdido", rescateSinFechaException.getMessage());
  }

  @Test
  public void crearRescateConAnimalSinChapita() {
    RuntimeException rescateAnimalSinChapita = assertThrows(RuntimeException.class, () -> {
      new Rescate(Fixture.rescatistaGenerico(), new AnimalPerdido(null, Collections.singletonList(new Foto()), "MAL", 1, 2), LocalDate.now());
    });
    assertEquals("Se debe ingresar animalPerdido con chapita", rescateAnimalSinChapita.getMessage());
  }
}
// segun lowy este test solo testeaba el framework, x lo tanto lo saco ya que no tiene importancia