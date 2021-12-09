package ar.edu.utn.frba.dds.test;

import ar.edu.utn.frba.dds.dominio.Adopcion.Detalle;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

public class DetalleTest {

  @Test
  public void detalleConPreguntaVaciaNoComprueba() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      new Detalle(null,"respuesta");
    });
  }

  @Test
  public void detalleConRespuestaVaciaNoComprueba() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      new Detalle("pregunta",null);
    });
  }

  @Test
  public void detalleConPreguntaYRespuestaVaciasNoComprueba() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      new Detalle(null,null);
    });
  }

  @Test
  public void detalleConPreguntaYRespuestaNoVaciasComprueba() {
    Assertions.assertDoesNotThrow(() ->{
      new Detalle("preguntilla","respuesta");
    });
  }

  @Test
  public void preguntasIgualesConDistintasRespuestasPresentanDiscrepancia() {
    List<Detalle> listaDePreguntasYRespuestas = new ArrayList<>();
    listaDePreguntasYRespuestas.add(new Detalle("pregunta1","respuesta1"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2","respuesta2"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2","respuesta2"));
    Detalle preguntaDiscrepante = new Detalle("pregunta1","respuesta2"); // La respuesta de esta pregunta es distinta a la de la lista
    Assert.assertFalse(preguntaDiscrepante.noPresentoDiscrepaciaCon(listaDePreguntasYRespuestas));
  }

  @Test
  public void preguntasDistintasConMismasRespuestasNoPresentanDiscrepancia() {
    List<Detalle> listaDePreguntasYRespuestas = new ArrayList<>();
    listaDePreguntasYRespuestas.add(new Detalle("pregunta1","respuesta1"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2","respuesta2"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2","respuesta2"));
    Detalle preguntaNoDiscrepante = new Detalle("pregunta4","respuesta1"); // La respuesta de pregunta4 es igual a la de pregunta1
    Assert.assertTrue(preguntaNoDiscrepante.noPresentoDiscrepaciaCon(listaDePreguntasYRespuestas));
  }

  @Test
  public void preguntaNoDiscrepaConListaVaciaDePreguntas() {
    List<Detalle> listaDePreguntasYRespuestas = new ArrayList<>();
    Detalle preguntaNoDiscrepante = new Detalle("pregunta1", "respuesta1"); // No importa si son iguales o distintas la respuesta con la pregunta porque la lista esta vacia
    Assert.assertTrue(preguntaNoDiscrepante.noPresentoDiscrepaciaCon(listaDePreguntasYRespuestas));
  }

  @Test
  public void preguntasIgualesConMismasRespuestasNoPresentanDiscrepancia() {
    List<Detalle> listaDePreguntasYRespuestas = new ArrayList<>();
    listaDePreguntasYRespuestas.add(new Detalle("pregunta1", "respuesta1"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2", "respuesta2"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2", "respuesta2"));
    Detalle preguntaNoDiscrepante = new Detalle("pregunta3","resupuesta2"); // La pregunta3 y su respuesta es igual a la pregunta2
    Assert.assertTrue(preguntaNoDiscrepante.noPresentoDiscrepaciaCon(listaDePreguntasYRespuestas));
  }

  @Test
  public void preguntasDistintasConDistintasRespuestasNoPresentanDiscrepancia() {
    List<Detalle> listaDePreguntasYRespuestas = new ArrayList<>();
    listaDePreguntasYRespuestas.add(new Detalle("pregunta1","respuesta1"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2","respuesta2"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2","respuesta2"));
    Detalle preguntaNoDiscrepante = new Detalle("pregunta4", "respuesta4"); // La respuesta de la pregunta4 es distinta a el resto de las preguntas
    Assert.assertTrue(preguntaNoDiscrepante.noPresentoDiscrepaciaCon(listaDePreguntasYRespuestas));
  }

  @Test
  public void preguntasIgualesSonCoincidentes() {
    List<Detalle> listaDePreguntasYRespuestas = new ArrayList<>();
    listaDePreguntasYRespuestas.add(new Detalle("pregunta1","respuesta1"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2","respuesta2"));
    listaDePreguntasYRespuestas.add(new Detalle("pregunta2","respuesta2"));
    Detalle preguntaCoincidente = new Detalle("pregunta1","respuesta1"); // La preguntaCoincidente es igual a pregunta1
    Assert.assertTrue(preguntaCoincidente.soyCoincidenteConList(listaDePreguntasYRespuestas));
  }

  @Test
  public void preguntasDistintasNoSonCoincidentes() {
    List<Detalle> listaDePreguntasYRespuestas = new ArrayList<>();
    listaDePreguntasYRespuestas.add(new Detalle("pregunta1","respuesta1"));
    Detalle preguntaCoincidente = new Detalle("pregunta2","respuesta2"); // La preguntaCoincidente es igual a pregunta1
    Assert.assertFalse(preguntaCoincidente.soyCoincidenteConList(listaDePreguntasYRespuestas));
  }

  @Test
  public void listaVaciaDePreguntasNoTienePreguntasCoincidentes() {
    List<Detalle> listaDePreguntasYRespuestas = new ArrayList<>();
    Detalle preguntaCoincidente = new Detalle("pregunta2","respuesta2");
    Assert.assertFalse(preguntaCoincidente.soyCoincidenteConList(listaDePreguntasYRespuestas));
  }



}
