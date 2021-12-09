package ar.edu.utn.frba.dds.dominio.Repos;

import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Rescate;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RepoRescate implements WithGlobalEntityManager {

  private static RepoRescate instance = new RepoRescate();

  public static RepoRescate getInstance() {
    if (instance == null) {
      instance = new RepoRescate();
    }
    return instance;
  }

  public List<Rescate> getRescates() {
    return entityManager().createQuery("from Rescate", Rescate.class).getResultList();
  }

  public void agregarRescate(Rescate rescate) {
    entityManager().persist(rescate);
  }

  public void quitarRescate(Rescate rescate){
    entityManager().remove(rescate);
  }

  public List<AnimalPerdido> listarAnimalesEncontradosUltimos10Dias() {
    return this.listarRescateUltimos10Dias().stream().map(Rescate::getAnimalPerdido).collect(Collectors.toList());
  }

  private List<Rescate> listarRescateUltimos10Dias() {
    return this.getRescates().stream().filter(r -> this.estaEnRango(r.getFecha())).collect(Collectors.toList());
  }

  private boolean estaEnRango(LocalDate fecha) {
    LocalDate hace10Dias = LocalDate.now().plusDays(-10);
    return hace10Dias.isEqual(fecha) || fecha.isAfter(hace10Dias);
  }
}
