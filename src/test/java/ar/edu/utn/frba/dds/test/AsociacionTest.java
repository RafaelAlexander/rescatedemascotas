package ar.edu.utn.frba.dds.test;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.Adopcion.Adopcion;
import ar.edu.utn.frba.dds.dominio.Adopcion.Adoptante;
import ar.edu.utn.frba.dds.dominio.Adopcion.Detalle;
import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AsociacionTest {
  Asociacion nuevaAsociacion = new Asociacion(1,1);



  @Test
  public void validarQueSeCalculaBienLaDistanciaAMascota() throws SQLException, IOException {
    Foto nuevaFoto = Fixture.fotosGenericas().stream().findFirst().orElse(null);
    List<Foto> listaDeFotos = new ArrayList<>();
    listaDeFotos.add(nuevaFoto);
    AnimalPerdido mascotaPerdida = new AnimalPerdido(null, listaDeFotos, "Mal",1,2);
    assertEquals(nuevaAsociacion.distanciaAAnimal(mascotaPerdida),1);
  }

  @Test
  public void notificarAdoptantes() throws NotificacionException {
    AtomicBoolean pasoPorClaseSinError = new AtomicBoolean(false);
    nuevaAsociacion = mock(Asociacion.class);
    doAnswer(invocation->{
      pasoPorClaseSinError.set(true);
      return null;
    }).when(nuevaAsociacion).notificarAdoptantes();
    this.nuevaAsociacion.notificarAdoptantes();
    Assertions.assertTrue(pasoPorClaseSinError.get());
  }

  @Test
  public void NoHayAdopcionesCoincidentesParaAdoptanteNoApto(){
    List<Detalle> detallesDeAdoptante = Collections.singletonList(new Detalle("pregunta", "respuesta"));
    List<Detalle> detallesDeAdopcion1 = Collections.singletonList(new Detalle("pregunta", "respuesta2"));
    List<Detalle> detallesDeAdopcion2 = Collections.singletonList(new Detalle("pregunta", "respuesta3"));
    nuevaAsociacion.setPreguntasAdopcion(Collections.singletonList("pregunta"));
    Adoptante adoptante = new Adoptante(detallesDeAdoptante, nuevaAsociacion, "adoptante@gmail.com");
    Adopcion adopcion = new Adopcion("1",Collections.singletonList(new Contacto("nombre","1234","contacto@gmail.com")),detallesDeAdopcion1,nuevaAsociacion);
    Adopcion adopcion2 = new Adopcion("1",Collections.singletonList(new Contacto("nombre","1234","contacto@gmail.com")),detallesDeAdopcion2,nuevaAsociacion);
    nuevaAsociacion.agregarAdopcion(adopcion);
    nuevaAsociacion.agregarAdopcion(adopcion2);
    Assertions.assertTrue(nuevaAsociacion.obtenerAdopcionesCoincidentesPara(adoptante).isEmpty());
  }

  @Test
  public void HayAlgunasAdopcionesCoincidentesParaAdoptanteApto(){
    List<Detalle> detallesDeAdoptante = Collections.singletonList(new Detalle("pregunta", "respuesta"));
    List<Detalle> detallesDeAdopcion2 = Collections.singletonList(new Detalle("pregunta", "respuesta3"));
    nuevaAsociacion.setPreguntasAdopcion(Collections.singletonList("pregunta"));
    Adoptante adoptante = new Adoptante(detallesDeAdoptante, nuevaAsociacion, "adoptante@gmail.com");
    Adopcion adopcion = new Adopcion("1",Collections.singletonList(new Contacto("nombre","1234","contacto@gmail.com")), detallesDeAdoptante,nuevaAsociacion);
    Adopcion adopcion2 = new Adopcion("1",Collections.singletonList(new Contacto("nombre","1234","contacto@gmail.com")),detallesDeAdopcion2,nuevaAsociacion);
    nuevaAsociacion.agregarAdopcion(adopcion);
    nuevaAsociacion.agregarAdopcion(adopcion2);
    Assertions.assertTrue(nuevaAsociacion.obtenerAdopcionesCoincidentesPara(adoptante).contains(adopcion));
    Assertions.assertFalse(nuevaAsociacion.obtenerAdopcionesCoincidentesPara(adoptante).contains(adopcion2));
  }

  @Test
  public void TodasLasAdopcionesSonCoincidentesSiAdoptanteEsApto(){
    List<Detalle> detallesDeAdoptante = Collections.singletonList(new Detalle("pregunta", "respuesta"));
    nuevaAsociacion.setPreguntasAdopcion(Collections.singletonList("pregunta"));
    Adoptante adoptante = new Adoptante(detallesDeAdoptante, nuevaAsociacion, "adoptante@gmail.com");
    Adopcion adopcion = new Adopcion("1",Collections.singletonList(new Contacto("nombre","1234","contacto@gmail.com")), detallesDeAdoptante,nuevaAsociacion);
    Adopcion adopcion2 = new Adopcion("1",Collections.singletonList(new Contacto("nombre","1234","contacto@gmail.com")),detallesDeAdoptante,nuevaAsociacion);
    nuevaAsociacion.agregarAdopcion(adopcion);
    nuevaAsociacion.agregarAdopcion(adopcion2);
    Assertions.assertTrue(nuevaAsociacion.obtenerAdopcionesCoincidentesPara(adoptante).contains(adopcion));
    Assertions.assertTrue(nuevaAsociacion.obtenerAdopcionesCoincidentesPara(adoptante).contains(adopcion2));
  }

}
