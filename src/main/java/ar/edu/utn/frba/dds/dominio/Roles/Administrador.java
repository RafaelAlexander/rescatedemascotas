package ar.edu.utn.frba.dds.dominio.Roles;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Caracteristica;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Administrador {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	public Long getId() {
		return id;
	}

	public void agregarCaracteristicaARepositorio(Caracteristica caracteristica) {
		RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);
	}

}
