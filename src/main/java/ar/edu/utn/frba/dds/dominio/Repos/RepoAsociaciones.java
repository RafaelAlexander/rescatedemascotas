package ar.edu.utn.frba.dds.dominio.Repos;

import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import extras.WithGlobalEntityManager;

import javax.persistence.EntityManager;
import java.util.List;

public class RepoAsociaciones implements WithGlobalEntityManager {

  private EntityManager en = entityManager();

  private static RepoAsociaciones instance = new RepoAsociaciones();

  public static RepoAsociaciones getInstance() {
    return instance;
  }

  public void agregarEntity(EntityManager en) {
    this.en = en;
  }

  public void agregarAsociacion(Asociacion asociacion) {
    en.persist(asociacion);
  }

  public List<Asociacion> listaDeAsociaciones() {
    return en.createQuery("from Asociacion", Asociacion.class).getResultList();
  }

  public int distanciaDeLasociacionMasCercanaAUbicacion(AnimalPerdido animal) {
    //obtenemos la minima distancia
    return (this.listaDeAsociaciones().stream().mapToInt(asociacion -> asociacion.distanciaAAnimal(animal))).min().getAsInt();
  }

}
