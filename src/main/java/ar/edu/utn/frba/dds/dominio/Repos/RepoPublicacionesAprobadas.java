package ar.edu.utn.frba.dds.dominio.Repos;

import ar.edu.utn.frba.dds.dominio.Rescate.PublicacionAprobada;
import extras.WithGlobalEntityManager;
import javax.persistence.EntityManager;
import java.util.List;



public class RepoPublicacionesAprobadas implements WithGlobalEntityManager {

  private EntityManager en = entityManager();

  private static RepoPublicacionesAprobadas instance = new RepoPublicacionesAprobadas();

  public static RepoPublicacionesAprobadas getInstance() {
    return instance;
  }

  public void agregarManager(EntityManager en){
    this.en = en;
  }

  public void agregarPublicacion(PublicacionAprobada publicacionAprobada) {
    en.persist(publicacionAprobada);
  }

  public List<PublicacionAprobada> darPublicaciones() {
    return this.en.createQuery("from PublicacionAprobada", PublicacionAprobada.class).getResultList();
  }




}
