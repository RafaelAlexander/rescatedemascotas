package persistencia;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.*;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

public class MascotaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  public void persistirMascota() throws ClassNotFoundException, SQLException, IOException {
    Caracteristica caracteristica = new Caracteristica("asd",TipoCaracteristica.ES_BOOLEANA, null);
    CaracteristicaMascota caracteristicaMascota = new CaracteristicaMascota("asd","S");
    Contacto contacto = Fixture.contactoGenerico();
    RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);
    Duenio duenio = Fixture.duenioGenerico(contacto);
    Mascota mascota = new Mascota(
        TipoMascota.GATO,
        "Kumi",
        "kuchi",
        2,
        TipoSexo.HEMBRA,
        "flaca y grandota",
        Fixture.fotosGenericas(),
        Collections.singletonList(caracteristicaMascota), duenio);
    entityManager().persist(duenio);
    Assertions.assertNotNull(entityManager().find(Mascota.class, mascota.getId()));
  }
}
