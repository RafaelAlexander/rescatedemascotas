package ar.edu.utn.frba.dds.dominio.Adopcion;

import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.Repos.RepoNotificacion;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Adopcion {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;
  //TODO: el identificador, podriamos sacarselo ahora que agregamos el id ????

  private String identificador;//Para diferenciar puede ser nombre del animal, del dueñio o dejalo imaginar

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "contacto_id")
  private List<Contacto> contactos;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "contacto_id")
  private List<Detalle> detalles;

  public Adopcion(String identificador, List<Contacto> contactos, List<Detalle> detalles, Asociacion asociacion) {
    if (identificador.isEmpty()) {
      throw new RuntimeException("Debe colocar identificador a su solicitud para dar en adopcion");
    }
    this.identificador = identificador;
    if (contactos.isEmpty()) {
      throw new RuntimeException("Debe tener contactos para contactarlo, valga la reduncia");
    }
    this.contactos = contactos;

    this.verificarDetallesSonLosActuales(detalles, asociacion);

    this.detalles = detalles;

    asociacion.agregarAdopcion(this);
  }


  public String darDescripcionDeAdopcion() {
    return "Nombre de adopción : " + this.identificador + "\t" + this.contactos.stream().map(Contacto::darContacto).collect(Collectors.joining(",")) + ".";
  }

  private void verificarDetallesSonLosActuales(List<Detalle> detalles, Asociacion asociacion) {
    if (asociacion == null) {
      throw new RuntimeException("Debe indicar asociacion");
    }
    boolean estaActualizada = detalles.stream().allMatch(detalle -> detalle.preguntaHabilitadaSegunAsociacion(asociacion));
    if (detalles.isEmpty() || !estaActualizada) {
      throw new RuntimeException("Debe rellenar las respuestas actuales");
    }
  }

  public void notificarPorInteresado(String contactosInteresado) {
    RepoNotificacion.getInstance().getNotificadores().forEach(notificacion -> {
      try {
        notificacion.notificarQueHayAlguienInteresadoEnAdoptar(contactosInteresado, this.contactos);
      } catch (NotificacionException e) {
        throw new RuntimeException(e.getMessage());
      }
    });
  }
}
