package ar.edu.utn.frba.dds.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.Adopcion.Adopcion;
import ar.edu.utn.frba.dds.dominio.Adopcion.Adoptante;
import ar.edu.utn.frba.dds.dominio.Adopcion.Detalle;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.Notificacion.NotificacionPorCorreo;
import ar.edu.utn.frba.dds.dominio.Repos.RepoNotificacion;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import ar.edu.utn.frba.dds.dominio.Rescate.ParticipanteRescate;
import ar.edu.utn.frba.dds.dominio.Rescate.PublicacionAprobada;

public class AdopcionTest {

  private Adopcion adopcion;
  private Adoptante adoptante;

  @BeforeEach
  public void setUp() {
    //Datos de la persona que da en adopcion una mascota
    Contacto contacto = new Contacto("Diego Maradona", "1125784596", "maradona.diego.10@gmail.com");
    List<Contacto> contactos = new ArrayList<>();
    contactos.add(contacto);
    Detalle detalle = new Detalle("¿Le gustan los gatos?", "Sí, le gustan los gatos");
    List<Detalle> detalles = new ArrayList<>();
    detalles.add(detalle);
    Asociacion asociacion = new Asociacion(10.0, 20.0);
    List<String> preguntasAdopcion = new ArrayList<>();
    preguntasAdopcion.add("¿Le gustan los gatos?");
    asociacion.setPreguntasAdopcion(preguntasAdopcion);
    ParticipanteRescate participanteRescate = new ParticipanteRescate(Fixture.datosPersonales());
    AnimalPerdido animalPerdido = new AnimalPerdido(Long.valueOf(1), Collections.singletonList(new Foto()), "mojado", 10.0, 20.0);
    PublicacionAprobada publicacion = new PublicacionAprobada(participanteRescate, animalPerdido);

    //Datos de la persona adopta la mascota
    adoptante = new Adoptante(detalles, asociacion, "perezJose@gmail.com");
    asociacion.agregarAdoptante(adoptante);
    asociacion.agregarPublicacion(publicacion);
    adopcion = new Adopcion("1111", contactos, detalles, asociacion);

  }

  @Test
  @Disabled
  public void notificarPorInteresado() {
    NotificacionPorCorreo notificacionPorCorreo = new NotificacionPorCorreo();
    RepoNotificacion.getInstance().agregarNotificador(notificacionPorCorreo);
    adopcion.notificarPorInteresado(adoptante.getEmail());
    Assertions.assertEquals("Nombre de adopción : 1111	maradona.diego.10@gmail.com : 1125784596.", adopcion.darDescripcionDeAdopcion());
  }

}