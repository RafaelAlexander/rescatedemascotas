package ar.edu.utn.frba.dds.dominio.Adopcion;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Mascota;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

//https://github.com/spotbugs/spotbugs/issues/511
@Entity
@NoArgsConstructor
@DiscriminatorValue("Adopcion con duenio")
public class AdopcionDuenio extends Adopcion {

  @OneToOne(cascade = CascadeType.ALL)
  private Mascota mascota;

  public AdopcionDuenio(String identificador, List<Contacto> contactos, List<Detalle> detalles, Asociacion asociacion, Mascota mascota) {
    super(identificador,contactos,detalles,asociacion);
    this.mascota = mascota;
  }

  @Override
  public String darDescripcionDeAdopcion(){
   return "Nombre de mascota: " + this.mascota.getNombre() + "\t" + super.darDescripcionDeAdopcion();
  }
}
