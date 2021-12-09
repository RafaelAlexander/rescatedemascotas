package ar.edu.utn.frba.dds.dominio.Rescate;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class ParticipanteRescate {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  @OneToOne(cascade = CascadeType.MERGE)
  private DatosPersonales datosPersonales;

  public ParticipanteRescate(DatosPersonales datosPersonales) {
    if (datosPersonales == null) {
      throw new RuntimeException("Usted no coloco datos personales para ser participante");
    }
    this.datosPersonales = datosPersonales;
  }


  public String darContactosFormatoString() {
    Set<String> contactosString = this.datosPersonales.getContactos().stream().map(Contacto::getEmail).filter(c -> !c.isEmpty()).collect(Collectors.toSet());
    List<String> contactosTelefono = this.datosPersonales.getContactos().stream().map(Contacto::getTelefono).filter(c -> !c.isEmpty()).collect(Collectors.toList());
    contactosString.addAll(contactosTelefono);
    return contactosString.stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  public List<Contacto> getContactos() {
    return this.datosPersonales.getContactos();
  }
}