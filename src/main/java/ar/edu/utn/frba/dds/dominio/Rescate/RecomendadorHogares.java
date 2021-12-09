package ar.edu.utn.frba.dds.dominio.Rescate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.dominio.Criterios.Criterio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.ClientResponse;

public class RecomendadorHogares {

  private ApiRefugiosDDS apiRefugios;
  private List<Hogar> hogares;

  public RecomendadorHogares(ApiRefugiosDDS apiRefugios) throws JsonProcessingException, IOException {
    this.apiRefugios = apiRefugios;
    this.hogares = this.getHogares();
  }

  private List<Hogar> getHogares() throws JsonProcessingException, IOException {
    List<Hogar> hogares = new ArrayList<Hogar>();
    ClientResponse recurso = this.apiRefugios.getHogares();
    String jsonResultApi = recurso.getEntity(String.class);
    JsonNode JsonNodeApi = new ObjectMapper().readTree(jsonResultApi);
    JsonNode JsonNodeHogares = JsonNodeApi.get("hogares");
    for (JsonNode jsonHogar : JsonNodeHogares) {
      Hogar hogar = this.generarHogar(jsonHogar);
      hogares.add(hogar);
    }
    return hogares;
  }

  private Hogar generarHogar(JsonNode jsonHogar) {
    Hogar hogar = new Hogar();
    hogar.setNombre(jsonHogar.get("nombre").textValue());
    hogar.setPosX(jsonHogar.get("ubicacion").get("long").asDouble());
    hogar.setPosY(jsonHogar.get("ubicacion").get("lat").asDouble());
    boolean permitePerros = jsonHogar.get("admisiones").get("perros").asBoolean();
    boolean permiteGatos = jsonHogar.get("admisiones").get("gatos").asBoolean();
    if (permiteGatos && permitePerros) {
      hogar.setTipoAnimalesPermitidos(TipoAnimalesPermitidos.AMBOS);
    } else if (permiteGatos) {
      hogar.setTipoAnimalesPermitidos(TipoAnimalesPermitidos.GATO);
    } else {
      hogar.setTipoAnimalesPermitidos(TipoAnimalesPermitidos.PERRO);
    }
    hogar.setPoseePatio(jsonHogar.get("patio").asBoolean());
    if (jsonHogar.get("capacidad").asInt() > jsonHogar.get("lugares_disponibles").asInt()) {
      hogar.setAceptaPorDisponibilidad(true);
    }
    List<String> caracteristicas = new ArrayList<String>();
    jsonHogar.get("caracteristicas").iterator().forEachRemaining(c -> caracteristicas.add(c.asText()));
    hogar.setCaracteristicasHogar(caracteristicas);
    return hogar;
  }

  public List<Hogar> recomendar(List<Criterio> criterios) throws JsonProcessingException, IOException {
    List<Hogar> hogaresPorCriterios = this.hogares.stream().filter(hogar -> this.cumpleCriterios(hogar, criterios)).collect(Collectors.toList());
    return hogaresPorCriterios;
  }

  private boolean cumpleCriterios(Hogar hogar, List<Criterio> criterios) {
    return criterios.stream().allMatch(criterio -> criterio.cumple(hogar));
  }

}
