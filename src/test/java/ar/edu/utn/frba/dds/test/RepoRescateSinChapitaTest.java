package ar.edu.utn.frba.dds.test;


import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.Repos.RepoRescateSinChapita;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.RescateSinChapita;
import ar.edu.utn.frba.dds.dominio.Rescate.ParticipanteRescate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RepoRescateSinChapitaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  public void obtenerRescateSinChapitaLuegoDeHaberloPersistido() throws SQLException, IOException {
    DatosPersonales datosPersonales = Fixture.datosPersonales();
    entityManager().persist(datosPersonales);
    RescateSinChapita rescateSinChapita = new RescateSinChapita(new ParticipanteRescate(datosPersonales), this.nuevoAnimalEncontrado());
    RepoRescateSinChapita.getInstance().agregarRescateSinChapita(rescateSinChapita);
    assertEquals(RepoRescateSinChapita.getInstance().getRescatesSinChapita().get(0).getAnimalPerdido().getDescripcionEstado(), "Normal");
  }


  @Test
  public void verificarQueSoloHayPersisitidoUnRescate() throws SQLException, IOException {
    DatosPersonales datosPersonales = Fixture.datosPersonales();
    entityManager().persist(datosPersonales);
    RescateSinChapita rescateSinChapita = new RescateSinChapita(new ParticipanteRescate(datosPersonales), this.nuevoAnimalEncontrado());
    RepoRescateSinChapita.getInstance().agregarRescateSinChapita(rescateSinChapita);
    assertEquals(RepoRescateSinChapita.getInstance().cantidadDeRescatesActualmente(), 1);
  }


  private AnimalPerdido nuevoAnimalEncontrado() throws SQLException, IOException {
    Foto fotuli = Fixture.fotosGenericas().stream().findFirst().orElse(null);
    ArrayList<Foto> fotitos = new ArrayList<>();
    fotitos.add(fotuli);
    return new AnimalPerdido(Long.valueOf(1), fotitos, "Normal", 0, 0);
  }
}
