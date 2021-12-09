package ar.edu.utn.frba.dds.dominio.Excepciones;

public class NotificacionException extends Exception{

  public NotificacionException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotificacionException(String message) {
    super(message);
  }
}
