package ar.edu.utn.frba.dds.dominio.MascotaYDuenio;

import java.util.List;
import javax.persistence.*;

import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
public class Mascota {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long chapita;

  private String nombre;
  private String apodo;
  private int edadAproximada;
  private String descripcionFisica;

  @Enumerated
  private TipoMascota tipoMascota;

  @Enumerated
  private TipoSexo tipoSexo;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name= "mascota_id")
  private List<Foto> fotos;

  @ElementCollection
  private List<CaracteristicaMascota> caracteristicas;

  public Mascota(TipoMascota tipoMascota,
                 String nombre,
                 String apodo,
                 int edadAproximada,
                 TipoSexo tipoSexo,
                 String descripcionFisica,
                 List<Foto> fotos,
                 List<CaracteristicaMascota> caracteristicas,
                 Duenio duenio) {
    this.chapita = chapita;
    this.tipoMascota = tipoMascota;
    this.nombre = nombre;
    this.apodo = apodo;
    this.edadAproximada = edadAproximada;
    this.tipoSexo = tipoSexo;
    this.descripcionFisica = descripcionFisica;
    this.fotos = fotos;
    if (!caracteristicas.stream().allMatch(caracteristica -> RepoCaracteristicas.getInstance().caracteristicaAdmitida(caracteristica))) {
      throw new RuntimeException("Todas las caracteristicas necesarias para crear la mascota, deben estar en el REPOSITORIO DE CARACTERISTICAS");
    }
    this.caracteristicas = caracteristicas;
    duenio.registrarMascota(this);
  }

  public String getApodo() {
    return apodo;
  }

  public Long getId() {
    return chapita;
  }
}