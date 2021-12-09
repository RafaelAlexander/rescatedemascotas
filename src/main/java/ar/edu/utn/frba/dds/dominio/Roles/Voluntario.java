package ar.edu.utn.frba.dds.dominio.Roles;

import ar.edu.utn.frba.dds.dominio.Repos.RepoAsociaciones;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import ar.edu.utn.frba.dds.dominio.Rescate.RescateSinChapita;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class Voluntario {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  public Long getId() {
    return id;
  }



  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "asociacion_id")
  Asociacion asociacionAsignada;

  public Voluntario(Asociacion asociacionAsignada) {
    if (asociacionAsignada == null) {
      throw new RuntimeException("Debe tener una asociacion asignada");
    }
    this.asociacionAsignada = asociacionAsignada;
  }

  public void aprobarPublicacion(RescateSinChapita publicacion) {
    //aca deberiamos anotar las condiciones para en caso de que sea valido, apruebe finalmente la publicacion pendiente
    this.validarAsociacionAsignada(publicacion);
    asociacionAsignada.aprobar(publicacion);
  }


  private void validarAsociacionAsignada(RescateSinChapita rescateSinChapita) {
    // validamos si la asociacion asignada tiene una posicion cercana a un animal rescatado
    if (asociacionAsignada.distanciaAAnimal(rescateSinChapita.getAnimalPerdido()) != RepoAsociaciones.getInstance().distanciaDeLasociacionMasCercanaAUbicacion(rescateSinChapita.getAnimalPerdido())) {
      throw new RuntimeException("No es la asociacion mas cercana a la mascota encontrada");
    }
  }

  public Asociacion getAsociacionAsignada() {
    return asociacionAsignada;
  }
}


