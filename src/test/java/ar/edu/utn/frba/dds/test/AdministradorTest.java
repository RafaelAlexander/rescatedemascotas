package ar.edu.utn.frba.dds.test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Caracteristica;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoCaracteristica;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import ar.edu.utn.frba.dds.dominio.Roles.Administrador;

public class AdministradorTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @Test
  public void agregarCaracteristicaARepo() {
    Administrador administrador = new Administrador();
    Caracteristica caracteristica = new Caracteristica("Hola mundo", TipoCaracteristica.ES_BOOLEANA, null);
    administrador.agregarCaracteristicaARepositorio(caracteristica);
    assertTrue(RepoCaracteristicas.getInstance().estaDentroDeCaracteristicas(caracteristica));
  }
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }
  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }
}
