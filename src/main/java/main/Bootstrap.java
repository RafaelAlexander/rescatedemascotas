package main;

import java.time.LocalDate;
import java.util.Collections;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.*;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import ar.edu.utn.frba.dds.dominio.Repos.RepoDatosPersonales;
import ar.edu.utn.frba.dds.dominio.Repos.RepoUsuarios;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;

public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

	public static void main(String[] args) {
		new Bootstrap().run();
	}

	public void run() {
		withTransaction(() -> {

			Caracteristica caracteristica = new Caracteristica("Es bellisimo",TipoCaracteristica.ES_BOOLEANA,null);
			RepoCaracteristicas.getInstance().agregarCaracteristica(caracteristica);

			Usuario usuarioAdmin = new Usuario("admin","Administrador2021");
			usuarioAdmin.convertirseEnAdmin();
			RepoUsuarios.instancia.agregar(usuarioAdmin);

			DatosPersonales datosPersonales = new DatosPersonales("Silvia",
					"Escudero",
					TipoDocumento.RUC,
					"11323",
					Collections.singletonList(new Contacto(null,"1232","ASD@ASD")),
					LocalDate.now(),
					"ASD 1324");
			RepoDatosPersonales.getInstance().guardar(datosPersonales);
			Usuario usuario = new Usuario("duenioMascota","DuenioMascota2021");
			try {
				usuario.setCuentaDeDuenio(new Duenio(datosPersonales));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			RepoUsuarios.instancia.agregar(usuario);
		});
	}

}
