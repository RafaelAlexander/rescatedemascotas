package ar.edu.utn.frba.dds.dominio.MascotaYDuenio;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@IdClass(ClaveDeDatosPersonales.class)
public class DatosPersonales {
  @Id
  private String nombre;
  @Id
  private String apellido;
  @Id
  @Enumerated(EnumType.STRING)
  private TipoDocumento tipoDocumento;
  @Id
  private String numeroDeDocumento;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumns({
      @JoinColumn(name = "dp_nombre"),
      @JoinColumn(name = "dp_apellido"),
      @JoinColumn(name = "dp_tipoDoc"),
      @JoinColumn(name = "dp_nroDoc"),
  })
  private List<Contacto> contactos;

  private LocalDate fechaDeNacimiento;

  private String direccion;

  public DatosPersonales(String nombre,
                         String apellido,
                         TipoDocumento tipoDocumento,
                         String numeroDeDocumento,
                         List<Contacto> contactos,
                         LocalDate fechaDeNacimiento,
                         String direccion) {
    verificarDatosPersonales(nombre, apellido, tipoDocumento, numeroDeDocumento, contactos, fechaDeNacimiento, direccion);
    this.nombre = nombre;
    this.apellido = apellido;
    this.tipoDocumento = tipoDocumento;
    this.numeroDeDocumento = numeroDeDocumento;
    this.contactos = contactos;
    this.fechaDeNacimiento = fechaDeNacimiento;
    this.direccion = direccion;
  }

  public DatosPersonales() {

  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public String getNumeroDeDocumento() {
    return numeroDeDocumento;
  }

  public TipoDocumento getTipoDocumento() {
    return tipoDocumento;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public List<Contacto> getContactos() {
    return contactos;
  }

  public void addContacto(Contacto contacto) {
    this.contactos.add(contacto);
  }

  public void removeContacto(Contacto contacto) {
    this.contactos.remove(contacto);
  }

  public LocalDate getFechaDeNacimiento() {
    return fechaDeNacimiento;
  }

  public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
    this.fechaDeNacimiento = fechaDeNacimiento;
  }

  public ClaveDeDatosPersonales getId() {
    return new ClaveDeDatosPersonales(
        this.nombre,
        this.apellido,
        this.tipoDocumento,
        this.numeroDeDocumento
    );
  }

  public void setId(ClaveDeDatosPersonales pks) {
    this.nombre = pks.getNombre();
    this.apellido = pks.getApellido();
    this.tipoDocumento = pks.getTipoDocumento();
    this.numeroDeDocumento = pks.getNumeroDeDocumento();
  }


  private void verificarDatosPersonales(String nombre,
                                        String apellido,
                                        TipoDocumento tipoDocumento,
                                        String numeroDeDocumento,
                                        List<Contacto> contactos,
                                        LocalDate fechaDeNacimiento,
                                        String direccion) {
    if (nombre.isEmpty() ||
        apellido.isEmpty() ||
        tipoDocumento == null ||
        numeroDeDocumento.isEmpty() ||
        contactos.isEmpty() ||
        contactos == null ||
        fechaDeNacimiento == null ||
        direccion.isEmpty())
      throw new RuntimeException("Usted no relleno todos los campos");
  }
}
