package ar.edu.utn.frba.dds.dominio.Rescate;

import java.util.List;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class AnimalPerdido {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "animalPerdido_id")
  private List<Foto> fotos;

  private Long chapita;
  private String descripcionEstado;
  private double posX;
  private double posY;

  public AnimalPerdido(Long chapita, List<Foto> fotos, String descripcionEstado, double posX, double posY) {
    validacion(fotos, descripcionEstado);
    this.chapita = chapita;
    this.fotos = fotos;
    this.descripcionEstado = descripcionEstado;
    this.posX = posX;
    this.posY = posY;
  }

  private void validacion(List<Foto> fotos, String descripcionEstado) {
    if (fotos == null || fotos.size() == 0) {
      throw new RuntimeException("Se debe adjuntar al menos una foto del animal perdido");
    }

    if (descripcionEstado == null || descripcionEstado.isEmpty()) {
      throw new RuntimeException("Se debe ingresar estado del animal encontrado");
    }

  }

  public String getDescripcionEstado() {
    return descripcionEstado;
  }

  public List<Foto> getFotosAnimal() {
    return fotos;
  }

  public double getPosicionX() {
    return posX;
  }

  public double getPosicionY() {
    return posY;
  }

}
