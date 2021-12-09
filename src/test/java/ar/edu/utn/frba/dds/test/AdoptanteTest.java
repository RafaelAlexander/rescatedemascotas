package ar.edu.utn.frba.dds.test;

import ar.edu.utn.frba.dds.dominio.Adopcion.Adoptante;
import ar.edu.utn.frba.dds.dominio.Adopcion.Detalle;
import ar.edu.utn.frba.dds.dominio.Repos.RepoNotificacion;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdoptanteTest {

  private Adoptante adoptante;
  private Asociacion asociacion;

  @BeforeEach
  public void setUp() {
		RepoNotificacion.getInstance().eliminarNotificadores();
  	//Datos de adoptante y asociacion
		Detalle detalle = new Detalle("¿Le gustan los gatos?", "Sí, le gustan los gatos");
		List<Detalle> detalles = new ArrayList<>();
		detalles.add(detalle);

		asociacion = new Asociacion(10.0, 20.0);
		List<String> preguntasAdopcion = new ArrayList<>();
		preguntasAdopcion.add("¿Le gustan los gatos?");
		asociacion.setPreguntasAdopcion(preguntasAdopcion);

		adoptante = new Adoptante(detalles, asociacion, "perezJose@gmail.com");
		asociacion.agregarAdoptante(adoptante);
  }

	@Test
	public void elMailDelAdoptanteEsValido() {
  	new Adoptante(adoptante.getDetalleList(), asociacion, "esvalido@gmail.com");
	}

	@Test
	public void elMailDelAdoptanteEsInvalido() {
		RuntimeException exception = assertThrows(RuntimeException.class,() -> new Adoptante(adoptante.getDetalleList(), asociacion, "esInvalido.com"));
		Assertions.assertTrue(exception.getMessage().contains("Debe colocar un email valido"));
	}

	@Test
	public void laAsociacionNoPuedeSerNull() {
		RuntimeException exception = assertThrows(RuntimeException.class,() -> new Adoptante(adoptante.getDetalleList(), null, "esvalido@gmail.com"));
		Assertions.assertTrue(exception.getMessage().contains("Debe indicar asociacion"));
	}

	@Test
	public void losDetallesNoPuedenEstarVacios() {
		Asociacion asociacionX = new Asociacion(10.0, 20.0);
  	List<String> preguntasAdopcion = new ArrayList<>();
		preguntasAdopcion.add("¿Le gustan los gatos?");
		asociacionX.setPreguntasAdopcion(preguntasAdopcion);
		RuntimeException exception = assertThrows(RuntimeException.class,() -> new Adoptante(Collections.emptyList(), asociacionX, "esvalido@gmail.com"));
		Assertions.assertTrue(exception.getMessage().contains("Debe rellenar las respuestas actuales"));
	}

	@Test
	public void losDetallesDelAdoptanteNoCoinciden() {
		Asociacion asociacionX = new Asociacion(10.0, 20.0);
		List<String> preguntasAdopcion = new ArrayList<>();
		preguntasAdopcion.add("¿Le gustan los perros?");
		asociacionX.setPreguntasAdopcion(preguntasAdopcion);
		RuntimeException exception = assertThrows(RuntimeException.class,() -> new Adoptante(adoptante.getDetalleList(), asociacionX, "esvalido@gmail.com"));
		Assertions.assertTrue(exception.getMessage().contains("Debe rellenar las respuestas actuales"));
	}

	@Test
	public void EsAptoParaAdoptarConTodosLosDetallesCoincidentes() {
		Detalle detalle = new Detalle("¿Le gustan los gatos?", "Sí, le gustan los gatos");
		Detalle detalle2 = new Detalle("pregunta", "respuesta");
		List<Detalle> detallesDeAdopcion = Arrays.asList(detalle,detalle2);
		adoptante.setDetalleList(detallesDeAdopcion);
		assertTrue(adoptante.esAptoParaAdoptarA(detallesDeAdopcion));
	}

	@Test
	public void NoEsAptoParaAdoptarConAlgunosDetallesNoCoincidentes() {
		Detalle detalle = new Detalle("¿Le gustan los gatos?", "No");
		Detalle detalle2 = new Detalle("pregunta", "respuesta");
		List<Detalle> detallesDeAdopcion = Arrays.asList(detalle,detalle2);
		adoptante.setDetalleList(Arrays.asList(new Detalle("¿Le gustan los gatos?", "SI"),new Detalle("pregunta", "respuesta")));
		assertFalse(adoptante.esAptoParaAdoptarA(detallesDeAdopcion));
	}

	@Test
	public void NoEsAptoParaAdoptarConNingunDetalleCoincidente() {
		Detalle detalle = new Detalle("¿Le gustan los gatos?", "No");
		Detalle detalle2 = new Detalle("pregunta", "respuesta");
		List<Detalle> detallesDeAdopcion = Arrays.asList(detalle,detalle2);
		adoptante.setDetalleList(Arrays.asList(new Detalle("¿Le gustan los gatos?", "SI"),new Detalle("pregunta", "respuesta2")));
		assertFalse(adoptante.esAptoParaAdoptarA(detallesDeAdopcion));
	}
}