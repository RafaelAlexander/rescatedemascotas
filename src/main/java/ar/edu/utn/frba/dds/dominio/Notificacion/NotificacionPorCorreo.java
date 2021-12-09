package ar.edu.utn.frba.dds.dominio.Notificacion;

import ar.edu.utn.frba.dds.dominio.Adopcion.Adopcion;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NotificacionPorCorreo implements Notificacion {

  private final String username = System.getenv("DDS2021_EMAILSERVICE_USER");
  private final String pass = System.getenv("DDS2021_EMAILSERVICE_PASS");

  private Session abrirSesion() {
    // Propiedades de la sesion con TLS
    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true"); //TLS
    prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    return Session.getInstance(prop,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, pass);
          }
        });
  }

  private Message construirCorreo(Session session, String destinatario, String asunto, String cuerpo) throws MessagingException {
    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(username));
    message.setRecipients(
        Message.RecipientType.TO,
        InternetAddress.parse(destinatario)
    );
    message.setSubject(asunto);
    message.setText(cuerpo);
    return message;
  }

  public void enviarCorreo(String destinatario, String asunto, String cuerpo) throws NotificacionException {
    if (!Objects.equals(destinatario, "") && destinatario != null) {
      Session session = abrirSesion();
      try {
        if (cuerpo == null) {
          throw new NotificacionException("El cuerpo del mensaje es nulo, ingrese un cuerpo");
        }
        Message message = construirCorreo(session, destinatario, asunto, cuerpo);
        Transport.send(message);
      } catch (SendFailedException e) {
        if (e.getMessage().equals("Invalid Addresses")) {
          throw new NotificacionException("Remitentes invalidos: " + Arrays.toString(e.getInvalidAddresses()), e);
        }
      } catch (MessagingException e) { // Agregar excepciones mas especificas
        e.printStackTrace();
      }
    } else {
      throw new NotificacionException("El destinario esta vacio, ingrese un destinario");
    }
  }


  // Implementaciones

  @Override
  public void notificarMascotaEncontrada(AnimalPerdido mascotaEncontrada, List<Contacto> contactosAnotificar) throws NotificacionException {
    String asunto = "MASCOTA ENCONTRADA";
    String cuerpo = "Su mascota con chapita " + mascotaEncontrada.getChapita() + " fue encontrada!";
    this.mandarMensajes(contactosAnotificar, asunto, cuerpo);
  }

  private List<String> darEmails(List<Contacto> contactos) {
    return contactos.stream().map(Contacto::getEmail).filter(NotificacionPorCorreo::emailValido).collect(Collectors.toList());
  }

  public static boolean emailValido(String email) {
    Pattern pattern = Pattern
        .compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    Matcher mather = pattern.matcher(email);
    return mather.find();
  }

  @Override
  public void notificarRescatistaDuenioEncontrado(String contactosDuenio, List<Contacto> contactosAnotificar) throws NotificacionException {
    String asunto = "Duenio Encontrado";
    String cuerpo = "Se puede contactar con el supuestoDuenio en los sig: " + contactosDuenio;
    this.mandarMensajes(contactosAnotificar, asunto, cuerpo);
  }

  @Override
  public void notificarQueHayAlguienInteresadoEnAdoptar(String contactosInteresado, List<Contacto> contactosAnotificar) throws NotificacionException {
    String asunto = "Adoptante interesado";
    String cuerpo = "Se puede contactar con el interesado en los sig contactos: " + contactosInteresado;
    this.mandarMensajes(contactosAnotificar, asunto, cuerpo);
  }

  @Override
  public void notificarPublicacionesCoincidentes(List<Adopcion> adopcionesCoincidentes, List<Contacto> contactosAnotificar) throws NotificacionException {
    String asunto = "Adopciones disponibles segun sus respuestas";
    String cuerpo = adopcionesCoincidentes.stream().map(Adopcion::darDescripcionDeAdopcion).collect(Collectors.joining(",")) + ".";
    mandarMensajes(contactosAnotificar,asunto,cuerpo);
  }

  private void mandarMensajes(List<Contacto> contactosAnotificar, String asunto, String cuerpo) throws NotificacionException {
    List<String> emails = this.darEmails(contactosAnotificar);
    if (!contactosAnotificar.isEmpty()) {
      for (String email : emails) {
        enviarCorreo(email, asunto, cuerpo);
      }
    } else {
      throw new NotificacionException("Su lista de emails esta vacia");
    }
  }

  @Override
  public boolean soyServicioDeCorreo() {
    return true;
  }
}
