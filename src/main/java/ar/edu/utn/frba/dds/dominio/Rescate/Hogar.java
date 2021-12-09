package ar.edu.utn.frba.dds.dominio.Rescate;

import java.util.ArrayList;
import java.util.List;


import ar.edu.utn.frba.dds.dominio.Criterios.Criterio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
o Algunos hogares solamente aceptan perros, otros solamente gatos y a otros les da lo mismo.
o Algunos hogares poseen patios y otros no. Si poseen patio, aceptan mascotas medianas o grandes; mientras que si no poseen, solamente aceptan mascotas chicas.
o Puede que un hogar no tenga disponibilidad por estar con su capacidad completa.
o El rescatista podrá elegir el radio de cercanía de los hogares de tránsito.
o Algunos hogares son más específicos para la admisión de mascotas y deben considerarse características puntuales.
*/

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hogar {
	
  @JsonProperty("nombre")
  private String nombre;

  @JsonProperty("long")
  private double posX;

  @JsonProperty("lat")
  private double posY;

  private TipoAnimalesPermitidos tipoAnimalesPermitidos;
  private boolean aceptaPorDisponibilidad;
  private boolean poseePatio;
  
  private List<String> caracteristicasHogar = new ArrayList<String>();
  
  public boolean cumpleCriterio(Criterio criterio) {
	  return criterio.cumple(this);
  }
  
}
