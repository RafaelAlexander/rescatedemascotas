package ar.edu.utn.frba.dds.dominio.MascotaYDuenio;

import ar.edu.utn.frba.dds.dominio.Adopcion.AdopcionDuenio;
import ar.edu.utn.frba.dds.dominio.Adopcion.Adoptante;
import ar.edu.utn.frba.dds.dominio.Adopcion.Detalle;
import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.Notificacion.Notificacion;
import ar.edu.utn.frba.dds.dominio.Repos.RepoNotificacion;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
public class Duenio{

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  public Long getId() {
    return id;
  }

  @OneToOne(cascade = CascadeType.MERGE)
  private DatosPersonales datosPersonales;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "duenio_id")
  private List<Mascota> mascotas = new ArrayList<>();

  @ElementCollection
  private List<String> notificaciones = new ArrayList<>();

  public Duenio(DatosPersonales datosPersonales) throws ClassNotFoundException {
    if(datosPersonales == null)
      throw new RuntimeException("Usted no ingreso datos personales en Duenio");
    this.datosPersonales = datosPersonales;
  }


  public void registrarMascota(Mascota mascota) {
    if (this.mascotas.contains(mascota)) {
      throw new RuntimeException("Usted no puede agregar a una mascota ya agregada al Sistema");
    }
    mascotas.add(mascota);
  }

  public Boolean estaChapitaPerteneceAUnaMascotaMia(Long chapita) {
    return !this.mascotas.stream().filter(mascotaMia -> mascotaMia.getId().equals(chapita)).collect(Collectors.toList()).isEmpty();
  }

  public void alertaDeRescate(AnimalPerdido animalPerdido) throws NotificacionException {
    for (Notificacion notificacion : RepoNotificacion.getInstance().getNotificadores()) {
      notificacion.notificarMascotaEncontrada(animalPerdido, this.datosPersonales.getContactos());
    }
  }

  public Adoptante quieroSerAdoptante(List<Detalle> detalles, Asociacion asociacion) {
    String email = this.datosPersonales.getContactos().stream().map(Contacto::getEmail).findFirst().orElse(null);
    if (email.isEmpty())
      throw new RuntimeException("Usted no tiene email elegible");
    return new Adoptante(detalles, asociacion, email);
  }

  public AdopcionDuenio quieroDarEnAdopcion(List<Detalle> detalles, Asociacion asociacion, Mascota mascota) {
    return new AdopcionDuenio(this.datosPersonales.getNombre() + " " + this.datosPersonales.getApellido(),
        this.datosPersonales.getContactos(),
        detalles,
        asociacion,
        mascota);
  }

}
