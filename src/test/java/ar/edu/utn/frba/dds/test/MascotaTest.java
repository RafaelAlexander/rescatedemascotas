package ar.edu.utn.frba.dds.test;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.*;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import ar.edu.utn.frba.dds.dominio.Repos.RepoUsuarios;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MascotaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  Duenio duenioGenerico = Fixture.duenioGenerico(Fixture.contactoGenerico());

  public MascotaTest() throws ClassNotFoundException {
  }

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }
  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }




  @Test
  public void MascotaSeAsocioDirectamenteConDuenio() throws ClassNotFoundException, SQLException, IOException {
    Caracteristica caracteristica = new Caracteristica("Color kiwi",TipoCaracteristica.ES_BOOLEANA, null);
    entityManager().persist(duenioGenerico.getDatosPersonales());
    RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);
    CaracteristicaMascota caracteristicaMascota = new CaracteristicaMascota("Color kiwi","S");
    Mascota mascota =  this.perro("Kumi",
        "kachi",
        2,
        "flaca y grandota",
        Fixture.fotosGenericas(),
        Collections.singletonList(caracteristicaMascota));
    //Solo para test
    entityManager().persist(duenioGenerico);
    assertTrue(duenioGenerico.estaChapitaPerteneceAUnaMascotaMia(mascota.getChapita()));
  }

  @Test
  public void elApodoDeUnaMascotaEsKuchi() throws ClassNotFoundException, SQLException, IOException {
    Caracteristica caracteristica = new Caracteristica("Color marron",TipoCaracteristica.ES_BOOLEANA, null);
    CaracteristicaMascota caracteristicaMascota = new CaracteristicaMascota("Color marron","S");
    RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);
    assertEquals(
        this.perro("Kumi",
            "kuchi",
            2,
            "flaca y grandota",
            Fixture.fotosGenericas(),
            Collections.singletonList(caracteristicaMascota)).getApodo(), "kuchi");
  }

  @Test
  public void crearMascotaConCaracteristicasNoRegistradas() {
    RuntimeException mascotaConCaracteristicasInvalidas = assertThrows(RuntimeException.class, () -> {
      this.perro("Kumi",
          "kuchi",
          2,
          "flaca y grandota",
          Fixture.fotosGenericas(),
          Collections.singletonList(new CaracteristicaMascota("Soy invalido","asdasdsa")));
    });
    assertEquals("Todas las caracteristicas necesarias para crear la mascota, deben estar en el REPOSITORIO DE CARACTERISTICAS", mascotaConCaracteristicasInvalidas.getMessage());
  }

  @AfterEach
  public void limpiarRepoCaracteristicas() {
    RepoCaracteristicas.getInstance().getCaracteristicas().clear();
  }

  public Mascota perro(String nombre, String apodo, int edad, String descripcionFisica, List<Foto> fotos, List<CaracteristicaMascota> caracteristicas) throws ClassNotFoundException {
    return new Mascota(TipoMascota.PERRO, nombre, apodo, edad, TipoSexo.MACHO, descripcionFisica, fotos, caracteristicas, this.duenioGenerico);
  }


}
