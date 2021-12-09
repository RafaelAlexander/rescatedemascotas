package ar.edu.utn.frba.dds.test;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import ar.edu.utn.frba.dds.dominio.Criterios.CriterioAceptaCapacidadDisponible;
import ar.edu.utn.frba.dds.dominio.Criterios.CriterioAceptaCercania;
import ar.edu.utn.frba.dds.dominio.Criterios.CriterioAceptaGatos;
import ar.edu.utn.frba.dds.dominio.Criterios.CriterioAceptaMascotasChicas;
import ar.edu.utn.frba.dds.dominio.Criterios.CriterioAceptaMascotasMedianasOGrandes;
import ar.edu.utn.frba.dds.dominio.Criterios.CriterioAceptaPerros;
import ar.edu.utn.frba.dds.dominio.Criterios.CriterioAceptaPerrosYGatos;
import ar.edu.utn.frba.dds.dominio.Rescate.ApiRefugiosDDS;
import ar.edu.utn.frba.dds.dominio.Rescate.Hogar;
import ar.edu.utn.frba.dds.dominio.Rescate.RecomendadorHogares;

public class RecomendadorHogaresTest {

  private RecomendadorHogares recomendadorHogares;
  private ApiRefugiosDDS apiRefugiosDDS;
  private CriterioAceptaPerros criterioAceptaPerros;
  private CriterioAceptaGatos criterioAceptaGatos;
  private CriterioAceptaPerrosYGatos criterioAceptaPerrosYGatos;
  private CriterioAceptaMascotasMedianasOGrandes criterioAceptaMascotasMedianasOGrandes;
  private CriterioAceptaMascotasChicas criterioAceptaMascotasChicas;
  private CriterioAceptaCapacidadDisponible criterioAceptaCapacidadDisponible;
  private CriterioAceptaCercania criterioAceptaCercania;

  @BeforeEach
  public void setUp() throws JsonProcessingException, IOException {
    this.apiRefugiosDDS = new ApiRefugiosDDS();
    this.recomendadorHogares = new RecomendadorHogares(this.apiRefugiosDDS);
    this.criterioAceptaPerros = new CriterioAceptaPerros();
    this.criterioAceptaGatos = new CriterioAceptaGatos();
    this.criterioAceptaPerrosYGatos = new CriterioAceptaPerrosYGatos();
    this.criterioAceptaMascotasMedianasOGrandes = new CriterioAceptaMascotasMedianasOGrandes();
    this.criterioAceptaMascotasChicas = new CriterioAceptaMascotasChicas();
    this.criterioAceptaCapacidadDisponible = new CriterioAceptaCapacidadDisponible();
    this.criterioAceptaCercania = new CriterioAceptaCercania(0.0, 0.0, 100);
   
  }

  @Test
  public void recomendarHogaresSoloPerros() throws JsonProcessingException, IOException {
	Hogar hogarRecomendado = this.recomendadorHogares.recomendar(Collections.singletonList(this.criterioAceptaPerros)).get(0);
    assertTrue(hogarRecomendado.cumpleCriterio(this.criterioAceptaPerros));
  }

  @Test
  public void recomendarHogaresSoloGatos() throws JsonProcessingException, IOException {
	Hogar hogarRecomendado = this.recomendadorHogares.recomendar(Collections.singletonList(this.criterioAceptaGatos)).get(0);
	assertTrue(hogarRecomendado.cumpleCriterio(this.criterioAceptaGatos));
  }

  @Test
  public void recomendarHogaresPerrosYGatos() throws JsonProcessingException, IOException {
	Hogar hogarRecomendado = this.recomendadorHogares.recomendar(Collections.singletonList(this.criterioAceptaPerrosYGatos)).get(0);
	assertTrue(hogarRecomendado.cumpleCriterio(this.criterioAceptaPerrosYGatos));
  }

  @Test
  public void recomendarHogaresParaMascotasMedianasOGrandes() throws JsonProcessingException, IOException {
    Hogar hogarRecomendado = this.recomendadorHogares.recomendar(Collections.singletonList(this.criterioAceptaPerrosYGatos)).get(0);
    assertTrue(hogarRecomendado.cumpleCriterio(this.criterioAceptaMascotasMedianasOGrandes));
  }

  @Test
  public void recomendarHogaresParaMascotasChicas() throws JsonProcessingException, IOException {
    Hogar hogarRecomendado = this.recomendadorHogares.recomendar(Collections.singletonList(this.criterioAceptaMascotasChicas)).get(0);
    assertTrue(hogarRecomendado.cumpleCriterio(this.criterioAceptaMascotasChicas));
  }

  @Test
  public void recomendarHogaresConDisponibilidad() throws JsonProcessingException, IOException {
    Hogar hogarRecomendado = this.recomendadorHogares.recomendar(Collections.singletonList(this.criterioAceptaCapacidadDisponible)).get(0);
    assertTrue(hogarRecomendado.cumpleCriterio(this.criterioAceptaCapacidadDisponible));
  }

  @Test
  public void recomendarHogaresCercanos() throws JsonProcessingException, IOException {
    Hogar hogarRecomendado = this.recomendadorHogares.recomendar(Collections.singletonList(this.criterioAceptaCercania)).get(0);
    assertTrue(hogarRecomendado.cumpleCriterio(this.criterioAceptaCercania));
  }
  
  @Test
  public void noRecomiendaHogaresLeganos() throws JsonProcessingException, IOException {
	this.criterioAceptaCercania = new CriterioAceptaCercania(0.0, 0.0, 10);
    assertTrue(this.recomendadorHogares.recomendar(Collections.singletonList(this.criterioAceptaCercania)).isEmpty());    
  }

}
