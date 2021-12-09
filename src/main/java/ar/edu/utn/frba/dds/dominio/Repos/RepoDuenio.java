package ar.edu.utn.frba.dds.dominio.Repos;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Duenio;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class RepoDuenio implements WithGlobalEntityManager {
  private static RepoDuenio instance = new RepoDuenio();

  public static RepoDuenio getInstance() {
    return instance;
  }

  public List<Duenio> getDuenios() {
    return  entityManager().createQuery("from Duenio",Duenio.class).getResultList();
  }

  public void agregarDuenio(Duenio duenio){
    if (duenio.getId() == null) {
      entityManager().persist(duenio);
    } else {
      entityManager().merge(duenio);
    }
  }

  public Duenio mostrarDuenioDeMascotaSegun(Long chapita) {
    return this.getDuenios().stream().filter(duenio -> duenio.estaChapitaPerteneceAUnaMascotaMia(chapita)).collect(Collectors.toList()).stream().findFirst().orElse(null);
  }
}
