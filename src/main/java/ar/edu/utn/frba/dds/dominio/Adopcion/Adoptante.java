package ar.edu.utn.frba.dds.dominio.Adopcion;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.Notificacion.NotificacionPorCorreo;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Adoptante {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  private String email;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "adoptante_id")
  private List<Detalle> detalleList;


  public Adoptante(List<Detalle> detalles, Asociacion asociacion, String email) {
    if (!NotificacionPorCorreo.emailValido(email)) {
      throw new RuntimeException("Debe colocar un email valido");
    }
    this.email = email;

    verificarDetallesSonLosActuales(detalles, asociacion);

    asociacion.agregarAdoptante(this);

    this.detalleList = detalles;
  }



  public Contacto generarContacto(){
    return new Contacto("adoptante","0",email);
  }

  private void verificarDetallesSonLosActuales(List<Detalle> detalles, Asociacion asociacion) {
    if (asociacion == null) {
      throw new RuntimeException("Debe indicar asociacion");
    }
    boolean estaActualizada = asociacion.contestaTodasLasPreguntas(detalles);
    if (detalles.isEmpty() || !estaActualizada) {
      throw new RuntimeException("Debe rellenar las respuestas actuales");
    }
  }

  public boolean esAptoParaAdoptarA(List<Detalle> detallesDeAdopcion){
    return detallesDeAdopcion.stream().allMatch(detalle -> detalle.noPresentoDiscrepaciaCon(this.detalleList));
  }

}
