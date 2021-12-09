package ar.edu.utn.frba.dds.test;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Caracteristica;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoCaracteristica;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

public class RepoCaracteristicasTest extends AbstractPersistenceTest implements WithGlobalEntityManager{
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }
  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  void hayTresCaracteristicasAgregadasEnLaListaDeCaracteristicas() {
    RepoCaracteristicas.getInstance().agregarCaracteristica(castrado());
    RepoCaracteristicas.getInstance().agregarCaracteristica(colaCorta());
    RepoCaracteristicas.getInstance().agregarCaracteristica(colorPrimario());
    assertEquals(RepoCaracteristicas.getInstance().cantidadDeCaracteristicasEnLaLista(), 3);
  }

  @Test
  void caracteristicaAEstaListado() {
    RepoCaracteristicas.getInstance().agregarCaracteristica(castrado());
    RepoCaracteristicas.getInstance().agregarCaracteristica(colaCorta());
    Caracteristica caracteristicaListada = RepoCaracteristicas.getInstance().getCaracteristicas().get(0);
    assertEquals(caracteristicaListada.getDescripcion(), "castrado");
  }

  @Test
  void unaCaracteristicasEstaDentroDeLaListaDeCaracteristicas() {
    RepoCaracteristicas.getInstance().agregarCaracteristica(castrado());
    RepoCaracteristicas.getInstance().agregarCaracteristica(colaCorta());
    Caracteristica caracteristicaListadaA = RepoCaracteristicas.getInstance().getCaracteristicas().get(0);
    assertTrue(RepoCaracteristicas.getInstance().estaDentroDeCaracteristicas(caracteristicaListadaA), "La caracteristica A esta en la lista");
  }
  private Caracteristica castrado() {
    return new Caracteristica("castrado",TipoCaracteristica.ES_BOOLEANA, null);
  }

  private Caracteristica colaCorta() {
    return new Caracteristica("cola corta",TipoCaracteristica.ES_BOOLEANA, null);
  }

  private Caracteristica colorPrimario() {
    return new Caracteristica("color primario: gris",TipoCaracteristica.ES_BOOLEANA, null);
  }
}
