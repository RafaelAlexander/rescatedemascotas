package ar.edu.utn.frba.dds.dominio.Notificacion;

import ar.edu.utn.frba.dds.dominio.Adopcion.Adopcion;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.exception.AuthenticationException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.List;
import java.util.stream.Collectors;

public class NotificacionPorSMS implements Notificacion {

  private static final PhoneNumber from = new PhoneNumber("+16782562777"); // numero de telefono registrado en twilio
  private final String ACCOUNT_SID = System.getenv("DDS2021_SMSSERVICE_ACCOUNTSID");
  private final String AUTH_TOKEN = System.getenv("DDS2021_SMSSERVICE_AUTHTOKEN");

  public void enviarSMS(String destinatario, String cuerpo) throws AuthenticationException {
    // El formato correcto para un numero de argentina es "+5411" + el numero, por ejemplo +541133914938
    if (!destinatario.isEmpty()) {
      Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      try {
        Message.creator(
            new PhoneNumber(destinatario),
            from,
            cuerpo).
            create();
      } catch (ApiException e) {
        switch (e.getStatusCode()) {
          case 400:
            if (e.getMessage().contains("Message body is required.")) {
              throw new AuthenticationException("El cuerpo del mensaje no puede estar vacio.");
            } else {
              throw new AuthenticationException("El numero del destinatario es invalido.");
            }
          case 503:
            throw new AuthenticationException("El servidor no esta disponible, vuelta a intentar.");
          case 429:
            throw new AuthenticationException("El servidor no responde, vuela a intentar.");
          default:
            throw new AuthenticationException("Ha ocurrido un error, vuelva a intentar.");
        }
      }
    }
  }

  // Implementaciones

  @Override
  public void notificarMascotaEncontrada(AnimalPerdido mascotaEncontrada, List<Contacto> contactosAnotificar) throws NotificacionException {
    String cuerpo = "Su mascota con chapita " + mascotaEncontrada.getChapita() + " fue encontrada!";
    if (!contactosAnotificar.isEmpty()) {
      for (String telefono : obtenerTodosLosTelefonos(contactosAnotificar)) {
        enviarSMS(telefono, cuerpo);
      }
    }
  }

  @Override
  public void notificarRescatistaDuenioEncontrado(String contactosDuenio, List<Contacto> contactosAnotificar) throws NotificacionException {
    String cuerpo = "Duenio Encontrado. Se puede contactar con el supuestoDuenio en los sig: " + contactosDuenio;
    if (!contactosAnotificar.isEmpty()) {
      for (String telefono : obtenerTodosLosTelefonos(contactosAnotificar)) {
        enviarSMS(telefono, cuerpo);
      }
    }
  }

  @Override
  public void notificarQueHayAlguienInteresadoEnAdoptar(String contactosInteresado, List<Contacto> contactosAnotificar) throws NotificacionException {
    String cuerpo = "Persona interesada a adoptar. Se puede contactar con el interesado en los sig: " + contactosInteresado;
    if (!contactosAnotificar.isEmpty()) {
      for (String telefono : obtenerTodosLosTelefonos(contactosAnotificar)) {
        enviarSMS(telefono, cuerpo);
      }
    }
  }

  @Override
  public void notificarPublicacionesCoincidentes(List<Adopcion> adopcionesCoincidentes, List<Contacto> contactosAnotificar) {
    // nada ya que solo se notifica por correo
  }

  @Override
  public boolean soyServicioDeCorreo() {
    return false;
  }

  public List<String> obtenerTodosLosTelefonos(List<Contacto> contactos){
    return contactos.stream().map(Contacto::getTelefono).collect(Collectors.toList());
  }
}
