package ar.edu.utn.frba.dds.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import org.junit.jupiter.api.Test;
import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;

import java.io.IOException;
import java.sql.SQLException;

public class AnimalPerdidoTest {

  @Test
  public void creacionExitosaDelAnimalPerdido() throws SQLException, IOException {
    assertEquals(Fixture.animalPerdidoGenerico().getChapita(), Long.valueOf(1));
    assertTrue(Fixture.animalPerdidoGenerico().getFotos().size() > 0);
    assertEquals(Fixture.animalPerdidoGenerico().getDescripcionEstado(), "Estado generico");
    assertTrue(Fixture.animalPerdidoGenerico().getFotos().size() > 0);
  }

  @Test
  public void noAdjuntarFotosLanzaExepcion() {
    Exception exception = assertThrows(Exception.class,() -> new AnimalPerdido(Long.valueOf(1), null, "Estado generico", 0, 0));
    assertEquals(exception.getMessage(),"Se debe adjuntar al menos una foto del animal perdido");
  }

  @Test
  public void noAdjuntarEstadoLanzaExepcion() {
    Exception exception = assertThrows(Exception.class,() -> new AnimalPerdido(Long.valueOf(1), Fixture.fotosGenericas(), null, 0, 0));
    assertEquals(exception.getMessage(),"Se debe ingresar estado del animal encontrado");
  }

  @Test
  public void unAnimalPerdidoPuedeNoTenerChapita() throws SQLException, IOException {
    AnimalPerdido animal = new AnimalPerdido(null, Fixture.fotosGenericas(), "Estado generico", 0, 0);
    assertEquals(animal.getChapita(), null);
  }
}
