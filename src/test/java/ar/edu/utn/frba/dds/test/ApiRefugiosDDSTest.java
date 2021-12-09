package ar.edu.utn.frba.dds.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sun.jersey.api.client.ClientResponse;

import ar.edu.utn.frba.dds.dominio.Rescate.ApiRefugiosDDS;

public class ApiRefugiosDDSTest {

  ApiRefugiosDDS requester;

  @BeforeEach
  public void setUp() throws Exception {
    this.requester = new ApiRefugiosDDS();
  }

  @Test
  public void obtenerHogaresDeLaApiRefugiosDDSTest() throws Exception {
    ClientResponse response = this.requester.getHogares();
    assertEquals(response.getStatus(), 200);
    String json = response.getEntity(String.class);
    assertTrue(json.contains("hogares"));
  }
}