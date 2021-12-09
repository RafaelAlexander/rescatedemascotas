package ar.edu.utn.frba.dds.dominio.Criterios;

import ar.edu.utn.frba.dds.dominio.Rescate.Hogar;

public interface Criterio {
	public boolean cumple(Hogar hogar);
}
