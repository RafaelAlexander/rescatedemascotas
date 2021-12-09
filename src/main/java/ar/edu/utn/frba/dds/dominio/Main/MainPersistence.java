package ar.edu.utn.frba.dds.dominio.Main;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import com.fasterxml.jackson.core.JsonProcessingException;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Duenio;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;

public class MainPersistence {

  public static void main(String[] args) throws JsonProcessingException, IOException, ClassNotFoundException {

    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    DatosPersonales datosPersonales = new DatosPersonales("Diego",
        "Maradona",
        TipoDocumento.DNI,
        "1202121200",
        Collections.singletonList(new Contacto("Diego Maradona",
            "",
            "eldiegote10@gmail.com")),
        LocalDate.now(),
        "Av SiempreViva 123");
    entityManager.persist(new Duenio(datosPersonales));

    List<Duenio> duenios = entityManager.createQuery("from Duenio").getResultList();
    Duenio duenio = duenios.get(0);

    System.out.println("Nombre Duenio: " + duenio.getDatosPersonales().getNombre());

    transaction.commit();

  }

}
