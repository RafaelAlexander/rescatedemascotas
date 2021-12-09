package ar.edu.utn.frba.dds.test;

import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.Notificacion.NotificacionPorSMS;
import com.twilio.exception.AuthenticationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.*;

@Ignore
public class NotificacionPorSMSTest {
   NotificacionPorSMS notificacionPorSMS;
  @Rule
  public final  EnvironmentVariables environmentVariables
      = new EnvironmentVariables();

  @Before
  public void setUp(){
    environmentVariables.set("DDS2021_SMSSERVICE_ACCOUNTSID","AC7229de1c9e3c4ae1bc6ea4a025680ac8");
    environmentVariables.set("DDS2021_SMSSERVICE_AUTHTOKEN", "576ab32c1d8e2ba508d99f96ab24fc27");
    notificacionPorSMS = new NotificacionPorSMS();
  }

  @Test
  public void enviarSMSTest() throws NotificacionException {
    String destinatario = "+541155136689"; // por ahora solo funciona con este numero, para probar con otros destinatarios hay que verificarlos en twilio
    notificacionPorSMS.enviarSMS(destinatario, "Esto es un mensaje de prueba");
  }

  @Test
  public void enviarUnSMSaNumeroInvalidoDevuelveNotificacionException() {
    Exception exception = assertThrows(AuthenticationException.class, () -> notificacionPorSMS.enviarSMS("numero invalido", "cuerpo del mensaje"));
    assertEquals(exception.getMessage(), "El numero del destinatario es invalido.");
  }

  @Test
  public void enviarUnSMSsinCuerpoDevuelveNotificacionException() {
    String destinatario = "+541155136689";
    Exception exception = assertThrows(AuthenticationException.class, () -> notificacionPorSMS.enviarSMS(destinatario, null));
    assertTrue(exception.getMessage().contains("El cuerpo del mensaje no puede estar vacio."));
  }

}
