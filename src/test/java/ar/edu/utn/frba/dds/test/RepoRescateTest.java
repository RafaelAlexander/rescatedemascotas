package ar.edu.utn.frba.dds.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import ar.edu.utn.frba.dds.dominio.Rescate.ParticipanteRescate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.Repos.RepoRescate;
import ar.edu.utn.frba.dds.dominio.Rescate.Rescate;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RepoRescateTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  public void seRescataronAnimalesEnLosUltimos10Dias() throws SQLException, IOException {
    DatosPersonales datos = Fixture.datosPersonales();
    entityManager().persist(datos);
    ParticipanteRescate rescatista = new ParticipanteRescate(datos);
    RepoRescate.getInstance().agregarRescate(this.rescateA(rescatista));
    RepoRescate.getInstance().agregarRescate(this.rescateB(rescatista));
    assertEquals(RepoRescate.getInstance().listarAnimalesEncontradosUltimos10Dias().size(), 1);

  }

  @Test
  public void noSeRescataronAnimalesEnLosUltimos10Dias() {
    assertEquals(RepoRescate.getInstance().listarAnimalesEncontradosUltimos10Dias().size(), 0);
  }

  Rescate rescateA(ParticipanteRescate rescatista) throws SQLException, IOException {
    return new Rescate(rescatista, new AnimalPerdido(Long.valueOf(1), Fixture.fotosGenericas(), "Marron", 1, 2), LocalDate.now());
  }

  Rescate rescateB(ParticipanteRescate rescatista) throws SQLException, IOException {
    return new Rescate(rescatista, new AnimalPerdido(Long.valueOf(2), Fixture.fotosGenericas(), "Marron", 1, 2), LocalDate.now().minusDays(20));
  }
}