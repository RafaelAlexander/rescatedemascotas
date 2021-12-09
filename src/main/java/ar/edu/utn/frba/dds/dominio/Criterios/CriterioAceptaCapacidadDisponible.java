package ar.edu.utn.frba.dds.dominio.Criterios;

import ar.edu.utn.frba.dds.dominio.Rescate.Hogar;

public class CriterioAceptaCapacidadDisponible implements Criterio {

	@Override
	public boolean cumple(Hogar hogar) {
		return hogar.isAceptaPorDisponibilidad();
	}

}
