package ar.edu.utn.frba.dds.dominio.Rescate;


import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.Notificacion.Notificacion;
import ar.edu.utn.frba.dds.dominio.Repos.RepoNotificacion;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class PublicacionAprobada {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  private ParticipanteRescate rescatista;
  
  @OneToOne(cascade = CascadeType.ALL)
  private AnimalPerdido animalPerdido;
  
  @OneToOne(cascade = CascadeType.ALL)
  private ParticipanteRescate duenioReclamante;

  public PublicacionAprobada(ParticipanteRescate rescatista, AnimalPerdido animalPerdido) {
    this.rescatista = rescatista;
    this.animalPerdido = animalPerdido;
  }

  public void rellenarFormularioDuenio(ParticipanteRescate duenioReclamante) throws NotificacionException {
    if (this.duenioReclamante != null) {
      throw new RuntimeException("Ya hay un dueño que realizo esta tarea para esta mascota perdida");
    }
    this.duenioReclamante = duenioReclamante;
    this.notificarRescatista();
  }


  //INTERNOS
  private void notificarRescatista() throws NotificacionException {
    for (Notificacion notificacion : RepoNotificacion.getInstance().getNotificadores()) {
      notificacion.notificarRescatistaDuenioEncontrado(duenioReclamante.darContactosFormatoString(), this.rescatista.getContactos());
    }
  }

  private void cumpleCriterio(/*Criterio criterio*/) {
    //TODO: esto quiza para mas adelante como mencionastes LOWY, que para buscar una cierta publicacion de una mascota, que cumpla con algun criterio
  }

}
