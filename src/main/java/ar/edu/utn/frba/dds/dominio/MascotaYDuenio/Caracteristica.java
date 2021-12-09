package ar.edu.utn.frba.dds.dominio.MascotaYDuenio;

import lombok.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Caracteristica {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	private String descripcion;

	@Enumerated(EnumType.STRING)
	private TipoCaracteristica tipoCaracteristica;
	
	@ElementCollection
	private List<String> valores = new ArrayList<>();

	public Caracteristica(String descripcion, TipoCaracteristica tipoCaracteristica, List<String> valores){
		this.descripcion = descripcion;
		this.tipoCaracteristica = tipoCaracteristica;
		if (tipoCaracteristica == TipoCaracteristica.ES_BOOLEANA) {
			this.valores.add("S");
			this.valores.add("N");
		}else {
			this.valores = valores;
		}
	}

	public String getDescripcion() {
		return descripcion;
	}


	public List<String> getValores() {
		return valores;
	}

	public void agregarValorCaracteristica(String valor) {
		if (this.tipoCaracteristica == TipoCaracteristica.ES_BOOLEANA) {
			throw new RuntimeException("No se puede agregar valores a una caracteristica booleana.");
		}	
		this.valores.add(valor);
	}
	
	public void eliminarValorCaracteristica(String valor) {
		if (this.tipoCaracteristica == TipoCaracteristica.ES_BOOLEANA) {
			throw new RuntimeException("No se puede eliminar valores a una caracteristica booleana.");
		}	
		this.valores.remove(valor);
	}

}
