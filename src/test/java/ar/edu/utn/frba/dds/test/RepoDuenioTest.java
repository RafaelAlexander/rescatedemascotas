package ar.edu.utn.frba.dds.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.*;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import ar.edu.utn.frba.dds.dominio.Repos.RepoDuenio;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;

public class RepoDuenioTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  Usuario usuario;

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  void probarFiltradoSegunChapita() throws ClassNotFoundException, SQLException, IOException {
    DatosPersonales datosPersonales = new DatosPersonales("Hombre"
        ,"JK"
        ,TipoDocumento.DNI
        ,"1223132123"
        ,Collections.singletonList(new Contacto("asd","132213",null))
        ,LocalDate.now()
        ,"Av 13231");
    entityManager().persist(datosPersonales);
    usuario = new Usuario("RafaBenitez", "yE^hy3m!e*H#");
    Duenio duenio = new Duenio(datosPersonales);
    Caracteristica caracteristica = Fixture.caracteristicaGenerica();
    CaracteristicaMascota caracteristicaMascota = new CaracteristicaMascota("Es el mejor compañero","S");
    RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);

    Mascota mascota = new Mascota(TipoMascota.PERRO,
                            "Firulais",
                            "Firu",
                            11,
                            TipoSexo.NOSESABE,
                            "Grande con manchas",
                            Fixture.fotosGenericas(),
                            Collections.singletonList(caracteristicaMascota),
                            duenio);

    usuario.agregarCuentaDeDuenioNueva(duenio);
    entityManager().persist(usuario);
    assertEquals(RepoDuenio.getInstance().mostrarDuenioDeMascotaSegun(mascota.getChapita()), duenio);
  }
}
