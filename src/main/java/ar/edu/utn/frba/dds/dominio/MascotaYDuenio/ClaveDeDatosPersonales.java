package ar.edu.utn.frba.dds.dominio.MascotaYDuenio;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class ClaveDeDatosPersonales implements Serializable {
  private String nombre;
  private String apellido;
  private TipoDocumento tipoDocumento;
  private String numeroDeDocumento;

  public ClaveDeDatosPersonales(String nombre, String apellido, TipoDocumento tipoDocumento, String numeroDeDocumento) {
    this.verificarSinVacios(nombre, apellido, tipoDocumento, numeroDeDocumento);
    this.nombre = nombre;
    this.apellido = apellido;
    this.tipoDocumento = tipoDocumento;
    this.numeroDeDocumento = numeroDeDocumento;
  }

  private void verificarSinVacios(String nombre, String apellido, TipoDocumento tipoDocumento, String numeroDeDocumento) {
    if (nombre.isEmpty() || apellido.isEmpty() || tipoDocumento == null || numeroDeDocumento.isEmpty())
      throw new RuntimeException("Usted no relleno todos los campos");

  }


  private ClaveDeDatosPersonales() {

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    ClaveDeDatosPersonales that = (ClaveDeDatosPersonales) o;
    return Objects.equals(nombre, that.nombre) && Objects.equals(apellido, that.apellido) && tipoDocumento == that.tipoDocumento && Objects.equals(numeroDeDocumento, that.numeroDeDocumento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre, apellido, tipoDocumento, numeroDeDocumento);
  }
}
