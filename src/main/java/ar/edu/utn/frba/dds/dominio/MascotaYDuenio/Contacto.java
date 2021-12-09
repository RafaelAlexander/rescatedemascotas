package ar.edu.utn.frba.dds.dominio.MascotaYDuenio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class Contacto {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  private String nombreApellido;
  private String telefono;
  private String email;



  public Contacto(String nombreApellido, String telefono, String email) {
    if (nombreApellido == null && telefono == null && email == null) {
      throw new RuntimeException("Todos los campos del contacto deben estar rellenados");
    }
    this.nombreApellido = nombreApellido;
    this.telefono = telefono;
    this.email = email;
  }

  public Long getId(){
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getTelefono() {
    return telefono;
  }

  public String darContacto(){
    return email + " : " + telefono;
  }
}