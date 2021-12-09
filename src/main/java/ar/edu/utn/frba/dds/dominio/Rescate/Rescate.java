package ar.edu.utn.frba.dds.dominio.Rescate;

import java.time.LocalDate;
import javax.persistence.*;

import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.Repos.RepoDuenio;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Rescate {
	
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  private ParticipanteRescate rescatista;
  
  @OneToOne(cascade = CascadeType.ALL)
  private AnimalPerdido animalPerdido;
  
  private LocalDate fecha;

  public Rescate(ParticipanteRescate rescatista, AnimalPerdido animalPerdido, LocalDate fecha) {
    if (animalPerdido.getChapita() == null ) {
      throw new RuntimeException("Se debe ingresar animalPerdido con chapita");
    }

    if (fecha == null) {
      throw new RuntimeException("Se debe ingresar la fecha en que se encontro el animal perdido");
    }
    this.rescatista = rescatista;
    this.animalPerdido = animalPerdido;
    this.fecha = fecha;
  }

  public void notificarDuenio() throws NotificacionException {
    RepoDuenio.getInstance().mostrarDuenioDeMascotaSegun(this.animalPerdido.getChapita()).alertaDeRescate(animalPerdido);
  }
}
