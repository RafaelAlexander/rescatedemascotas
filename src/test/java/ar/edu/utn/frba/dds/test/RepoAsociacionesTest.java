package ar.edu.utn.frba.dds.test;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.Repos.RepoAsociaciones;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class RepoAsociacionesTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }


  @Test
  public void consultarAsociaciones() {
    RepoAsociaciones.getInstance().agregarEntity(entityManager());

    Asociacion asociacion = new Asociacion(1, 2);
    RepoAsociaciones.getInstance().agregarAsociacion(asociacion);

    assertEquals(asociacion, RepoAsociaciones.getInstance().listaDeAsociaciones().stream().findFirst().orElse(null));
  }

  @Test
  public void agregamosAsociacionesYVemosSiLaMasCercanaEraLaQuEsperabamos() throws SQLException, IOException {
    RepoAsociaciones.getInstance().agregarEntity(entityManager());

    Asociacion asociacion1 = new Asociacion(1, 2);
    Asociacion asociacion2 = new Asociacion(2, 2);
    Asociacion asociacion3 = new Asociacion(0, 0);
    Asociacion asociacion4 = new Asociacion(3, 3);
    Asociacion asociacion5 = new Asociacion(4, 4);

    /*las persistimos*/
    RepoAsociaciones.getInstance().agregarAsociacion(asociacion1);
    RepoAsociaciones.getInstance().agregarAsociacion(asociacion2);
    RepoAsociaciones.getInstance().agregarAsociacion(asociacion3);
    RepoAsociaciones.getInstance().agregarAsociacion(asociacion4);
    RepoAsociaciones.getInstance().agregarAsociacion(asociacion5);

    /* aca creamos al animalPerdido */
    AnimalPerdido animalito = this.nuevoAnimalEncontrado();


    /*ahora pedimos el listado y como sabemos de antemano que la asociacion 3 es la que esta mas cerca,
    esta repo al preguntarle por la distancia de la asociacion mas cerca de las que estan persistidas,
    deberia traernos que sea la misma distancia a la de la asociacion 3 ,
    tambien podriamos preguntar que tenga la misma posicion que esta */

    assertEquals(asociacion3.distanciaAAnimal(animalito), RepoAsociaciones.getInstance().distanciaDeLasociacionMasCercanaAUbicacion(animalito));
    Assertions.assertEquals(0, RepoAsociaciones.getInstance().listaDeAsociaciones().get(2).getPosicionX());
    Assertions.assertEquals(0, RepoAsociaciones.getInstance().listaDeAsociaciones().get(2).getPosicionY());


  }

  private AnimalPerdido nuevoAnimalEncontrado() throws SQLException, IOException {
    Foto fotuli = Fixture.fotosGenericas().stream().findFirst().orElse(null);
    ArrayList<Foto> fotitos = new ArrayList<>();
    fotitos.add(fotuli);
    return new AnimalPerdido(Long.valueOf(1), fotitos, "Normal", 0, 0);
  }
}
