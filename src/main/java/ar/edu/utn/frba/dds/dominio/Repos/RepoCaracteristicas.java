package ar.edu.utn.frba.dds.dominio.Repos;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Caracteristica;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.CaracteristicaMascota;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;

public class RepoCaracteristicas implements WithGlobalEntityManager {

  private static RepoCaracteristicas instance = new RepoCaracteristicas();

  public static RepoCaracteristicas getInstance() {
    if (instance == null) {
      instance = new RepoCaracteristicas();
    }
    return instance;
  }

  public List<Caracteristica> getCaracteristicas() {
    return entityManager().createQuery("from Caracteristica", Caracteristica.class).getResultList();
  }


  public void agregarCaracteristica(Caracteristica caracteristica) {
    entityManager().persist(caracteristica);
  }

  public int cantidadDeCaracteristicasEnLaLista(){
    return this.getCaracteristicas().size();
  }

  public boolean estaDentroDeCaracteristicas(Caracteristica caracteristica) {
    return this.getCaracteristicas().contains(caracteristica);
  }

  public boolean caracteristicaAdmitida(CaracteristicaMascota caracteristica) {
    return this.getCaracteristicas().stream().anyMatch(c -> (c.getDescripcion().equals(caracteristica.getDescripcion()) && c.getValores().contains(caracteristica.getOpcionSeleccionada())));
  }

}
