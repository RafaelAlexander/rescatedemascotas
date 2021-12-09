package ar.edu.utn.frba.dds.dominio.Criterios;

import ar.edu.utn.frba.dds.dominio.Rescate.Hogar;
import ar.edu.utn.frba.dds.dominio.Rescate.TipoAnimalesPermitidos;

public class CriterioAceptaPerrosYGatos implements Criterio {

	@Override
	public boolean cumple(Hogar hogar) {
		return hogar.getTipoAnimalesPermitidos().equals(TipoAnimalesPermitidos.AMBOS);
	}

}
