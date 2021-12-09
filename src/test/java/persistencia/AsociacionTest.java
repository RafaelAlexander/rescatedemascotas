package persistencia;

import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class AsociacionTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @Test
  public void persistirAsociacion() {
    Asociacion asociacion = new Asociacion(1.0000001, 2.000001);
    entityManager().persist(asociacion);
    Asociacion asociacionPersistida = entityManager().find(Asociacion.class,asociacion.getId());
    Assertions.assertEquals(asociacion.getPosicionX(),asociacionPersistida.getPosicionX());
    Assertions.assertEquals(asociacion.getPosicionY(),asociacionPersistida.getPosicionY());
  }
}
