package ar.edu.utn.frba.dds.dominio.Rescate;

import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class ApiRefugiosDDS {

  private Client client;
  private static final String API_REFUGIOS = "https://api.refugiosdds.com.ar/api";
  private static final String RESOURCE_HOGARES = "hogares";

  public ApiRefugiosDDS() {
    this.client = Client.create();
  }

  public ClientResponse getHogares() {
    ClientResponse recurso = this.client.resource(API_REFUGIOS)
      .path(RESOURCE_HOGARES)
      .queryParam("offset", "1")
      .accept(MediaType.APPLICATION_JSON)
      .header("Authorization", "Bearer yTHlpGLjs6xDveoOUiVuS0tf6phpPE2NWcPiRJxC1LMcof2nqwUzcFkTpG0R")
      .get(ClientResponse.class);

      return recurso;
	}

}
