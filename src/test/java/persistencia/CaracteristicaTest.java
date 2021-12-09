package persistencia;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Caracteristica;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoCaracteristica;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class CaracteristicaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }
  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  public void persistirCaracteristica(){
    Caracteristica caracteristica = new Caracteristica("Hola mundo",TipoCaracteristica.ES_BOOLEANA, null);
    Caracteristica caracteristica2 = new Caracteristica("Hola mundo2",TipoCaracteristica.ES_BOOLEANA, null);
    RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);
    RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica2);
    Assertions.assertTrue(RepoCaracteristicas.getInstance().estaDentroDeCaracteristicas(caracteristica));
    Assertions.assertTrue(RepoCaracteristicas.getInstance().estaDentroDeCaracteristicas(caracteristica2));
  }

}
