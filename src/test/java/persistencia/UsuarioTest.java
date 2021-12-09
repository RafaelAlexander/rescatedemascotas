package persistencia;

import ar.edu.utn.frba.dds.dominio.Roles.Administrador;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class UsuarioTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }
  @Test
  public void persistenciaUsuario() throws ClassNotFoundException {
    Usuario usuario = new Usuario("RafaBenitez", "yE^hy3m!e*H#");
    entityManager().persist(usuario);
    Usuario persistido = entityManager().find(Usuario.class, usuario.getId());
    Assertions.assertNotNull(persistido);
  }
/*
  @Test
  public void agregarRolAUsuario() throws ClassNotFoundException {
    Usuario usuario = new Usuario("RafaBenitez", "yE^hy3m!e*H#");
    usuario.agregarRol(new Administrador());
    entityManager().persist(usuario);
    Usuario persistido = entityManager().find(Usuario.class, usuario.getId());
    Assertions.assertTrue(persistido.seleccionarRol(Administrador.class));
  }
*/



  @Test
  public void consultarAdministradoresTotalesSinUsuario() throws ClassNotFoundException {
    Usuario usuarioA = new Usuario("RafaBenitez", "yE^hy3m!e*H#");
    usuarioA.convertirseEnAdmin();
    entityManager().persist(usuarioA);
    Usuario usuarioB = new Usuario("RafaBenitez", "yE^hy3m!e*H#");
    usuarioB.convertirseEnAdmin();
    entityManager().persist(usuarioB);
    Assertions.assertEquals(2, entityManager().createQuery("from Administrador", Administrador.class).getResultList().size());
  }
}
