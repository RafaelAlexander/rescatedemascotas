package ar.edu.utn.frba.dds.dominio.Rescate;

import ar.edu.utn.frba.dds.dominio.Adopcion.Adopcion;
import ar.edu.utn.frba.dds.dominio.Adopcion.Adoptante;
import ar.edu.utn.frba.dds.dominio.Adopcion.Detalle;
import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.Repos.RepoNotificacion;
import ar.edu.utn.frba.dds.dominio.Repos.RepoPublicacionesAprobadas;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Asociacion {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  double posicionX;
  double posicionY;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "asociacion_id")
  List<PublicacionAprobada> publicacionesActuales = new ArrayList<>();

  @ElementCollection
  List<String> preguntasAdopcion = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "asociacion_id")
  List<Adopcion> adopciones = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "asociacion_id")
  List<Adoptante> adoptantes = new ArrayList<>();

  public Asociacion(double posicionX, double posicionY) {
    this.posicionX = posicionX;
    this.posicionY = posicionY;
  }

  public void aprobar(RescateSinChapita rescateSinChapita) {
    PublicacionAprobada nuevaPublicacionAprobada = rescateSinChapita.posiblePublicacionParaSerAprobada();
    RepoPublicacionesAprobadas.getInstance().agregarPublicacion(nuevaPublicacionAprobada);
    this.agregarPublicacion(nuevaPublicacionAprobada);
  }


  public void agregarPublicacion(PublicacionAprobada publicacionAAgregar) {
    publicacionesActuales.add(publicacionAAgregar);
  }

  public void agregarAdopcion(Adopcion adopcion) {
    this.adopciones.add(adopcion);
  }

  public void agregarAdoptante(Adoptante adoptante) {
    this.adoptantes.add(adoptante);
  }

  public int distanciaAAnimal(AnimalPerdido animal) {

    //calculo de radio mas cercano, metimos hasta una calculadora
    double distanciaX = Math.abs(this.posicionX - animal.getPosicionX());
    double distanciaY = Math.abs(this.posicionY - animal.getPosicionY());
    return (int) Math.sqrt(Math.pow(distanciaX, 2) + Math.pow(distanciaY, 2));

  }

  public void setPreguntasAdopcion(List<String> preguntasAdopcion) {
    this.preguntasAdopcion = preguntasAdopcion;
  }

  public int cantidadPublicacionesAprobadas() {
    return publicacionesActuales.size();
  }

  //Esto se debe hacer semanalmente
  public void notificarAdoptantes() throws NotificacionException {
    adoptantes.forEach(adoptante -> {
      List<Adopcion> adopcionesCoincidentes = obtenerAdopcionesCoincidentesPara(adoptante);
      if (!adopcionesCoincidentes.isEmpty()) {
        RepoNotificacion.getInstance().getNotificacionPorCorreo().forEach(notificacionPorCorreo -> {
          try {
            notificacionPorCorreo.notificarPublicacionesCoincidentes(adopcionesCoincidentes, Collections.singletonList(adoptante.generarContacto()));
          } catch (NotificacionException e) {
            e.printStackTrace();
          }
        });
      }
    });
  }

  public List<Adopcion> obtenerAdopcionesCoincidentesPara(Adoptante adoptante) {
    List<Adopcion> adopcionesCoincidentes = new ArrayList<>();
    adopciones.forEach(adopcion -> {
      if (adoptante.esAptoParaAdoptarA(adopcion.getDetalles())) {
        adopcionesCoincidentes.add(adopcion);
      }
    });
    return adopcionesCoincidentes;
  }

  public boolean preguntaHabilitada(String pregunta) {
    return preguntasAdopcion.contains(pregunta);
  }

  public boolean contestaTodasLasPreguntas(List<Detalle> detalles) {
    return detalles.stream().allMatch(detalle -> this.preguntaHabilitada(detalle.getPregunta()));
  }

  public Long getId() {
    return id;
  }
}
