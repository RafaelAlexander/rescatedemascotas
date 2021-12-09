package persistencia;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Duenio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class DuenioTest extends AbstractPersistenceTest implements WithGlobalEntityManager{
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }
  @Test
  public void persistirDuenio() throws ClassNotFoundException {
    Contacto contacto = Fixture.contactoGenerico();
    entityManager().persist(contacto);
    Duenio duenio  = Fixture.duenioGenerico(contacto);
    entityManager().persist(duenio);
    Duenio duenioPersistido = entityManager().find(Duenio.class,duenio.getId());
    Assertions.assertNotNull(duenioPersistido);
  }
}
