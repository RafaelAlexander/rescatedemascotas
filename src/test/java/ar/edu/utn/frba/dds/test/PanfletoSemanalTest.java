package ar.edu.utn.frba.dds.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.*;
import org.junit.jupiter.api.*;
import ar.edu.utn.frba.dds.dominio.Adopcion.AdopcionDuenio;
import ar.edu.utn.frba.dds.dominio.Adopcion.Adoptante;
import ar.edu.utn.frba.dds.dominio.Adopcion.Detalle;
import ar.edu.utn.frba.dds.dominio.Adopcion.PanfletoSemanal;
import ar.edu.utn.frba.dds.dominio.Notificacion.NotificacionPorCorreo;
import ar.edu.utn.frba.dds.dominio.Repos.RepoAsociaciones;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import ar.edu.utn.frba.dds.dominio.Repos.RepoNotificacion;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

public class PanfletoSemanalTest extends AbstractPersistenceTest implements WithGlobalEntityManager {
  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  public void TareaSemanal() throws ClassNotFoundException, SQLException, IOException {
    RepoAsociaciones.getInstance().agregarEntity(entityManager());
    PanfletoSemanal panfletoSemanal = mock(PanfletoSemanal.class);
    NotificacionPorCorreo notificacionPorCorreo = new NotificacionPorCorreo();
    RepoNotificacion.getInstance().agregarNotificador(notificacionPorCorreo);
    Asociacion asociacion = new Asociacion(1, 1);
    RepoAsociaciones.getInstance().agregarAsociacion(asociacion);
    asociacion.setPreguntasAdopcion(Collections.singletonList("?"));
    Detalle detalle = new Detalle("?", "asd");
    Duenio duenio = this.nuevoDuenio("Gabriel", "Acosta", LocalDate.now(),
        TipoDocumento.DNI, "55555555",
        Collections.singletonList(new Contacto("asd", "asdasd", "migue.racedo.oviedo@gmail.com")), "Av SimpreViva 123");
    Caracteristica caracteristica = new Caracteristica("Color marron",TipoCaracteristica.ES_BOOLEANA, null);
    CaracteristicaMascota caracteristicaMascota = new CaracteristicaMascota("Color marron","S");
    RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);
    Mascota mascota = new Mascota(TipoMascota.PERRO, "Kumi", "kuchi", 2, TipoSexo.MACHO, "flaca y grandota",
        Fixture.fotosGenericas(), Collections.singletonList(caracteristicaMascota), duenio);
    AdopcionDuenio adopcionDuenio = new AdopcionDuenio("asd",
        Collections.singletonList(new Contacto("RAFA", "1234", "migue.racedo.oviedo@gmail.com")),
        Collections.singletonList(detalle), asociacion, mascota);
    Adoptante adoptante = new Adoptante(Collections.singletonList(detalle), asociacion, "migue.racedo.oviedo@gmail.com");
    asociacion.agregarAdoptante(adoptante);
    asociacion.agregarAdopcion(adopcionDuenio);


    doAnswer((i) -> {
      panfletoSemanal.execute(null);
      return null;
    }).when(panfletoSemanal).comenzarEnvioDeAdopcionesValidasAInteresados(any());

    panfletoSemanal.comenzarEnvioDeAdopcionesValidasAInteresados(any());
    Assertions.assertTrue(RepoAsociaciones.getInstance().listaDeAsociaciones().size() > 0);
    Assertions.assertTrue(adopcionDuenio.darDescripcionDeAdopcion().contains("Kumi"));
  }

  public Duenio nuevoDuenio(String nombre, String apellido, LocalDate fechaNacimiento, TipoDocumento tipoDocumento, String nroDocumento, List<Contacto> contactos, String direccion) throws ClassNotFoundException {
    return new Duenio(new DatosPersonales(nombre, apellido, tipoDocumento, nroDocumento, contactos, fechaNacimiento, direccion));
  }
}
