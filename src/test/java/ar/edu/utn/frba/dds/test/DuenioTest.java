package ar.edu.utn.frba.dds.test;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.Adopcion.AdopcionDuenio;
import ar.edu.utn.frba.dds.dominio.Adopcion.Detalle;
import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.*;
import ar.edu.utn.frba.dds.dominio.Notificacion.Notificacion;
import ar.edu.utn.frba.dds.dominio.Notificacion.NotificacionPorCorreo;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import ar.edu.utn.frba.dds.dominio.Repos.RepoNotificacion;
import ar.edu.utn.frba.dds.dominio.Repos.RepoRescateSinChapita;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.jupiter.api.*;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DuenioTest extends AbstractPersistenceTest implements WithGlobalEntityManager {


  //TESTS CON EXCEPCIONES

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Rule
  public final static EnvironmentVariables environmentVariables
      = new EnvironmentVariables();

  @BeforeAll
  public static void setUp() {
    environmentVariables.set("DDS2021_EMAILSERVICE_USER", "grupo12.DDS2021@gmail.com");
    environmentVariables.set("DDS2021_EMAILSERVICE_PASS", "0JXly47Zr65h0c6yUP!dfX@r");
  }

  @Test
  public void chapitaAgena() throws ClassNotFoundException {
    assertFalse(Fixture.duenioGenerico(Fixture.contactoGenerico()).estaChapitaPerteneceAUnaMascotaMia(Long.valueOf(100000)));
  }

  @Test
  @Disabled
  public void seNotifico() throws ClassNotFoundException, NotificacionException, SQLException, IOException {
    Notificacion notificacionPorCorreo = new NotificacionPorCorreo();
    RepoNotificacion.getInstance().agregarNotificador(notificacionPorCorreo);
    Contacto contacto = new Contacto("NombreYApellido", "", "migue.racedo.oviedo@gmail.com");
    Duenio duenio = this.nuevoDuenio("Gaby", "Ronaldo", LocalDate.now(), TipoDocumento.DNI, "124", Collections.singletonList(contacto), "Av SiempreViva 123");
    duenio.alertaDeRescate(Fixture.animalPerdidoGenerico());
  }

  @Test
  public void darEnAdopcion() throws ClassNotFoundException, SQLException, IOException {
    Caracteristica caracteristica = new Caracteristica("Color kiwi", TipoCaracteristica.ES_BOOLEANA, null);
    CaracteristicaMascota caracteristicaMascota = new CaracteristicaMascota("Color kiwi", "S");
    RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);
    Duenio duenio = Fixture.duenioGenerico(Fixture.contactoGenerico());
    Mascota mascota = new Mascota(
        TipoMascota.PERRO,
        "Kumi",
        "kachi",
        2,
        TipoSexo.HEMBRA,
        "flaca y grandota",
        Fixture.fotosGenericas(),
        Collections.singletonList(caracteristicaMascota), duenio);
    Asociacion asociacion = new Asociacion(2, 3);
    asociacion.setPreguntasAdopcion(Collections.singletonList("Es malo?"));
    AdopcionDuenio adopcion = duenio.quieroDarEnAdopcion(Collections.singletonList(new Detalle("Es malo?", "No")), asociacion, mascota);
    assertEquals(adopcion.getIdentificador(), "Diego Maradona");
  }

  @AfterEach
  public void limpiarRepoDuenio() {
    RepoRescateSinChapita.getInstance().getRescatesSinChapita().clear();
    RepoNotificacion.getInstance().getNotificadores().clear();
  }


  public Duenio nuevoDuenio(String nombre, String apellido, LocalDate fechaNacimiento, TipoDocumento tipoDocumento, String nroDocumento, List<Contacto> contactos, String direccion) throws ClassNotFoundException {
    return new Duenio(new DatosPersonales(nombre, apellido, tipoDocumento, nroDocumento, contactos, fechaNacimiento, direccion));
  }
}
