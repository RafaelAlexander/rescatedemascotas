package ar.edu.utn.frba.dds.dominio.Rescate;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class RescateSinChapita {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  private ParticipanteRescate rescatista;
  @OneToOne(cascade = CascadeType.ALL)
  private AnimalPerdido animalPerdido;

  public RescateSinChapita(ParticipanteRescate rescatista, AnimalPerdido animalPerdido) {
    this.validacionDeDatos(rescatista, animalPerdido);
    this.rescatista = rescatista;
    this.animalPerdido = animalPerdido;
  }

  public PublicacionAprobada posiblePublicacionParaSerAprobada() {
    return new PublicacionAprobada(rescatista, animalPerdido);
  }

  public void validacionDeDatos(ParticipanteRescate rescatista, AnimalPerdido animalPerdido) {

    if (rescatista == null) {
      throw new RuntimeException("El rescatista no tiene los datos esperados");
    }
    if (animalPerdido == null) {
      throw new RuntimeException("El animal perdido no tiene los datos esperados");
    }
  }

  public AnimalPerdido getAnimalPerdido() {
    return animalPerdido;
  }

  public Long getId() {
    return id;
  }
}