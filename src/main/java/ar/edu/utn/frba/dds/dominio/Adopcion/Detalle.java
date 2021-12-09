package ar.edu.utn.frba.dds.dominio.Adopcion;

import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
public class Detalle {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  private String pregunta;
  private String respuesta;

  public Detalle(String pregunta,String respuesta){
    this.comprobarOracion(pregunta);
    this.comprobarOracion(respuesta);
    this.pregunta = pregunta;
    this.respuesta = respuesta;
  }

  private void comprobarOracion(String palabra){
    if(palabra.isEmpty()){
      throw new RuntimeException("Debe rellenar todos los campos");
    }
  }

  public boolean noPresentoDiscrepaciaCon(List<Detalle> detalleList){
    return detalleList.stream().filter(detalle -> this.respuestaNoCoincidente(detalle)).collect(Collectors.toList()).isEmpty();
  }

  public boolean soyCoincidenteConList(List<Detalle> detalles){
    return detalles.stream().anyMatch(detalle -> this.coincidenciaDeDetalles(detalle));
  }
  private boolean coincidenciaDeDetalles(Detalle otroDetalle){
      return pregunta.equals(otroDetalle.getPregunta()) && respuesta.equals(otroDetalle.getRespuesta());
  }
  private boolean respuestaNoCoincidente(Detalle otroDetalle) {
    return pregunta.equals(otroDetalle.getPregunta()) && !respuesta.equals(otroDetalle.getRespuesta());
  }


  public boolean preguntaHabilitadaSegunAsociacion(Asociacion asociacion){
    return asociacion.preguntaHabilitada(pregunta);
  }

}
