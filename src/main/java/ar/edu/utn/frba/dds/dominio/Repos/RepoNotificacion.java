package ar.edu.utn.frba.dds.dominio.Repos;

import ar.edu.utn.frba.dds.dominio.Notificacion.Notificacion;
import ar.edu.utn.frba.dds.dominio.Notificacion.NotificacionPorCorreo;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import extras.WithGlobalEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepoNotificacion {

  private List<Notificacion> notificadores = new ArrayList<>();

  private static RepoNotificacion INSTANCE = new RepoNotificacion();

  public static RepoNotificacion getInstance() {
    return INSTANCE;
  }

  public void agregarNotificador(Notificacion notificador) {
    this.notificadores.add(notificador);
  }

  public List<Notificacion> getNotificadores() {
    return notificadores;
  }

  public List<NotificacionPorCorreo> getNotificacionPorCorreo() {
    return this.getNotificadores().stream().filter(Notificacion::soyServicioDeCorreo).map(x -> (NotificacionPorCorreo) x).collect(Collectors.toList());
  }

  public void eliminarNotificadores(){
    notificadores.clear();
  }

}
