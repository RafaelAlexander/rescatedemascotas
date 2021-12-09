package ar.edu.utn.frba.dds.dominio.Notificacion;

import ar.edu.utn.frba.dds.dominio.Adopcion.Adopcion;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;

import java.util.List;

public interface Notificacion {

  void notificarMascotaEncontrada(AnimalPerdido mascotaEncontrada, List<Contacto> contactosAnotificar) throws NotificacionException;

  void notificarRescatistaDuenioEncontrado(String contactosDuenio, List<Contacto> contactosAnotificar) throws NotificacionException;

  void notificarQueHayAlguienInteresadoEnAdoptar(String contactosInteresado, List<Contacto> contactosAnotificar) throws NotificacionException;

  void notificarPublicacionesCoincidentes(List<Adopcion> adopcionesCoincidentes, List<Contacto> contactosAnotificar) throws NotificacionException;

  boolean soyServicioDeCorreo();
}
