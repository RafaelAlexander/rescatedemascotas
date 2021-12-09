package ar.edu.utn.frba.dds.dominio.Criterios;

import ar.edu.utn.frba.dds.dominio.Rescate.Hogar;

import java.util.List;

public class CriterioAceptaPorCaracteristicas implements Criterio {
	
	private List<String> caracteristicasBuscadas;
	
	public CriterioAceptaPorCaracteristicas(List<String> caracteristicasBuscadas) {
		this.caracteristicasBuscadas = caracteristicasBuscadas;
	}

	@Override
	public boolean cumple(Hogar hogar) {
		return hogar.getCaracteristicasHogar().containsAll(caracteristicasBuscadas);
	}

}
