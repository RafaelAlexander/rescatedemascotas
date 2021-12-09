package funciones;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import spark.Request;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public final class MapViews {

  private MapViews() {

  }

  public static void createMapRescate(Request request, DatosPersonales datosPersonales, Map<String, Object> modelo) {
    modelo.put("nombre", datosPersonales.getNombre());
    modelo.put("apellido", datosPersonales.getApellido());
    modelo.put("tipoDocumento", datosPersonales.getTipoDocumento().toString());
    modelo.put("nroDocumento", datosPersonales.getNumeroDeDocumento());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    modelo.put("fechaNacimiento", datosPersonales.getFechaDeNacimiento().format(formatter));
    Contacto contactoAux = datosPersonales.getContactos().stream().filter(contacto -> contacto.getTelefono() != null).findFirst().orElse(null);
    if (contactoAux != null)
      modelo.put("telefono", contactoAux.getTelefono());
    contactoAux = datosPersonales.getContactos().stream().filter(contacto -> contacto.getEmail() != null).findFirst().orElse(null);
    if (contactoAux != null)
      modelo.put("email", contactoAux.getEmail());
    modelo.put("direccion", datosPersonales.getDireccion());
  }

}
