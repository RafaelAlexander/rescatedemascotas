package persistencia;

import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import ar.edu.utn.frba.dds.dominio.Roles.Voluntario;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class VoluntarioTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @Test
  public void persistirVoluntario(){
    Voluntario rol = new Voluntario(new Asociacion(1,2));
    entityManager().persist(rol);
    Assertions.assertNotNull(entityManager().find(Voluntario.class,rol.getId()));
  }

  @Test
  public void guardaAsociacionEnVoluntario(){
    Asociacion asociacion = new Asociacion(1,2);
    entityManager().persist(asociacion);
    Voluntario rol = new Voluntario(asociacion);
    entityManager().persist(rol);
    Assertions.assertEquals(asociacion,entityManager().find(Voluntario.class,rol.getId()).getAsociacionAsignada());
  }
}
