package ar.edu.utn.frba.dds.dominio.Repos;
import ar.edu.utn.frba.dds.dominio.Rescate.RescateSinChapita;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import javax.persistence.EntityManager;
import java.util.List;


public class RepoRescateSinChapita implements WithGlobalEntityManager {
  private static RepoRescateSinChapita instance = new RepoRescateSinChapita();

  public static RepoRescateSinChapita getInstance() {
    return instance;
  }

  public List<RescateSinChapita> getRescatesSinChapita() {
    return entityManager().createQuery("from RescateSinChapita", RescateSinChapita.class).getResultList();
  }

  public void agregarRescateSinChapita(RescateSinChapita rescate) {
    entityManager().persist(rescate);
  }

  public int cantidadDeRescatesActualmente(){
    return this.getRescatesSinChapita().size();
  }
}
