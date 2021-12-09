package ar.edu.utn.frba.dds.dominio.Repos;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

public class RepoDatosPersonales implements WithGlobalEntityManager {
  private static RepoDatosPersonales instance = new RepoDatosPersonales();

  public static RepoDatosPersonales getInstance() {
    return instance;
  }

  public void guardar(DatosPersonales datosPersonales) {
    if (!entityManager().contains(datosPersonales)) {
      entityManager().persist(datosPersonales);
    } else {
      entityManager().merge(datosPersonales);
    }
  }
}
