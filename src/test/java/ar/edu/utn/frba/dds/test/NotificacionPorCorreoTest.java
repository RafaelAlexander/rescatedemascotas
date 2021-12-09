package ar.edu.utn.frba.dds.test;

import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.Notificacion.NotificacionPorCorreo;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NotificacionPorCorreoTest {

  static NotificacionPorCorreo notificacionPorCorreo;
  @Rule
  public final static EnvironmentVariables environmentVariables
      = new EnvironmentVariables();

  @BeforeAll
  public static void setUp(){
    environmentVariables.set("DDS2021_EMAILSERVICE_USER","grupo12.DDS2021@gmail.com");
    environmentVariables.set("DDS2021_EMAILSERVICE_PASS", "0JXly47Zr65h0c6yUP!dfX@r");
    notificacionPorCorreo = new NotificacionPorCorreo();
  }

  @Test
  public void enviarCorreoTest() throws NotificacionException {
    String destinatario = "hernan.gab.romero@gmail.com"; // funciona con cualquier destinatario
    notificacionPorCorreo.enviarCorreo(destinatario,"Prueba","Esto es un correo de prueba");
  }

  @Test
  public void SePuedeEnviarCorreoSinAsunto() throws NotificacionException {
    String destinatario = "migue.racedo.oviedo@gmail.com";
    notificacionPorCorreo.enviarCorreo(destinatario,null,"Esto es un correo de prueba");
  }

  @Test
  public void enviarCorreoConElDestinarioNullException(){
    NotificacionException exception = assertThrows(NotificacionException.class,() -> notificacionPorCorreo.enviarCorreo(null,"asunto","cuerpo"));
    assertTrue(exception.getMessage().contains("El destinario esta vacio, ingrese un destinario"));
  }

  @Test
  public void enviarCorreoSinCuerpoDevuelveUnaExcepcion(){
    String destinatario = "migue.racedo.oviedo@gmail.com";
    NotificacionException exception = assertThrows(NotificacionException.class,() -> notificacionPorCorreo.enviarCorreo(destinatario,"asunto",null));
    assertTrue(exception.getMessage().contains("El cuerpo del mensaje es nulo, ingrese un cuerpo"));
  }

  @Test
  public void enviarCorreoAUnaDirIncorrectaDaNotificacionException(){
    NotificacionException exception = assertThrows(NotificacionException.class,() -> notificacionPorCorreo.enviarCorreo("dir_incorrecta","asunto","cuerpo"));
    assertTrue(exception.getMessage().contains("Remitentes invalidos"));
  }

  @Test
  public void emailSinDominioEsInvalido() {
    Assertions.assertFalse(NotificacionPorCorreo.emailValido("username@"));
  }

  @Test
  public void emailConDominioSinSubdominioEsInvalido() {
    Assertions.assertFalse(NotificacionPorCorreo.emailValido("username@dominio"));
  }

  @Test
  public void emailSinUsernameEsInvalido() {
    Assertions.assertFalse(NotificacionPorCorreo.emailValido("@dominio"));
  }

  @Test
  public void emailSinArrobaEsInvalido() {
    Assertions.assertFalse(NotificacionPorCorreo.emailValido("usernamedominio"));
  }

  @Test
  public void emailConUsernameArrobaYDominioEsValido() {
    Assertions.assertTrue(NotificacionPorCorreo.emailValido("username@dominio.com"));
  }

}
