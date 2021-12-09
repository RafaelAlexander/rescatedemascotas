package ar.edu.utn.frba.dds.dominio.Criterios;

import ar.edu.utn.frba.dds.dominio.Rescate.Hogar;
import lombok.Data;

@Data
public class CriterioAceptaCercania implements Criterio {
	
	private double coordenadaCentroX;
	private double coordenadaCentroY;
	private int radio;
	
	public CriterioAceptaCercania(double coordenadaCentroX, double coordenadaCentroY, int radio) {
		this.coordenadaCentroX = coordenadaCentroX;
		this.coordenadaCentroY = coordenadaCentroY;
		this.radio = radio;
	}

	@Override
	public boolean cumple(Hogar hogar) {
		return estaDentroDelRadio(hogar.getPosX(),hogar.getPosY());
	}

	private boolean estaDentroDelRadio(double posHogarX, double posHogarY) {
		return ((posHogarX - this.getCoordenadaCentroX()) * (posHogarX - this.getCoordenadaCentroX()) + (posHogarY - this.getCoordenadaCentroY()) * (posHogarY - this.getCoordenadaCentroY())) < (this.getRadio() * this.getRadio());
	}

}
