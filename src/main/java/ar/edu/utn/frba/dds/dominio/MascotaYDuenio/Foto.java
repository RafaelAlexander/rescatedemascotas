package ar.edu.utn.frba.dds.dominio.MascotaYDuenio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Foto {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @Lob
  private Blob data;

  public Foto(Blob data) {
    this.data = data;
  }
}
