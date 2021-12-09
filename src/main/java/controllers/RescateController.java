package controllers;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.Repos.RepoDatosPersonales;
import ar.edu.utn.frba.dds.dominio.Repos.RepoRescate;
import ar.edu.utn.frba.dds.dominio.Repos.RepoRescateSinChapita;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.ParticipanteRescate;
import ar.edu.utn.frba.dds.dominio.Rescate.Rescate;
import ar.edu.utn.frba.dds.dominio.Rescate.RescateSinChapita;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import funciones.MapViews;
import funciones.NotificacionUsuario;
import funciones.SesionUsuario;
import org.apache.commons.io.IOUtils;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RescateController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public ModelAndView getFormularioRescate(Request request, Response response) {

    Usuario usuario = SesionUsuario.estaLogueado(request);
    Map<String, Object> modelo = new HashMap<>();

    if (usuario != null) {
      SesionUsuario.reconocerRol(usuario, modelo);
      modelo.put("sesion", "Estoy logueado");
      modelo.put("nombreUsuario", usuario.getUsername());
      if (usuario.getCuentaDeDuenio() != null) {
        MapViews.createMapRescate(request, usuario.getCuentaDeDuenio().getDatosPersonales(), modelo);
        modelo.put("sesion", "todo bien");
      }
    }

    return new ModelAndView(modelo, "registrarRescate.html.hbs");
  }

  public ModelAndView guardarRescate(Request request, Response response) {
    Map<String, Object> viewModel = new HashMap<String, Object>();
    Usuario usuario = SesionUsuario.estaLogueado(request);
    if (usuario != null) {
      viewModel.put("sesion", "Estoy logueado");
      viewModel.put("nombreUsuario", usuario.getUsername());
      SesionUsuario.reconocerRol(usuario, viewModel);
    }
    try {
      ParticipanteRescate rescatista = this.obtenerRescatista(request);
      AnimalPerdido animalPerdido = this.animalPerdido(request);
      if (animalPerdido.getChapita() != null) {
        Rescate rescate = new Rescate(rescatista, animalPerdido, LocalDate.now());
        withTransaction(() -> RepoRescate.getInstance().agregarRescate(rescate));
      } else {
        RescateSinChapita rescate = new RescateSinChapita(rescatista, animalPerdido);
        withTransaction(() -> RepoRescateSinChapita.getInstance().agregarRescateSinChapita(rescate));
      }
      NotificacionUsuario.notificar(viewModel, "success", "Éxito!: ", "Se ha registrado el rescate.");
    } catch (Exception exception) {
      NotificacionUsuario.notificar(viewModel, "danger", "Error: ", exception.getMessage());
    }
    return new ModelAndView(viewModel, "registrarRescate.html.hbs");
  }

  private String convertidorDeString(Request request, String nombre) throws ServletException, IOException {
    return IOUtils.toString(request.raw().getPart(nombre).getInputStream(), StandardCharsets.UTF_8);
  }

  private ParticipanteRescate obtenerRescatista(Request request) throws ServletException, IOException {
    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    String nombre = this.convertidorDeString(request, "nombre");
    String apellido = this.convertidorDeString(request, "apellido");
    String direccion = this.convertidorDeString(request, "direccion");
    String email = this.convertidorDeString(request, "email");
    String telefono = this.convertidorDeString(request, "telefono");
    LocalDate fechaNacimiento = LocalDate.parse(this.convertidorDeString(request, "fechaNacimiento"));
    TipoDocumento tipoDocumento = TipoDocumento.valueOf(this.convertidorDeString(request, "tipoDocumento"));
    String numeroDocumento = this.convertidorDeString(request, "nroDocumento");
    return new ParticipanteRescate(this.guardarDatosPersonales(nombre,
        apellido,
        tipoDocumento,
        numeroDocumento,
        new Contacto(null, telefono, email),
        fechaNacimiento,
        direccion));
  }

  private DatosPersonales guardarDatosPersonales(String nombre,
                                                 String apellido,
                                                 TipoDocumento tipoDocumento,
                                                 String numeroDocumento,
                                                 Contacto contacto,
                                                 LocalDate fechaNacimiento,
                                                 String direccion) {
    DatosPersonales datosPersonales = new DatosPersonales(nombre,
        apellido,
        tipoDocumento,
        numeroDocumento,
        Collections.singletonList(contacto),
        fechaNacimiento,
        direccion);
    withTransaction(() -> RepoDatosPersonales.getInstance().guardar(datosPersonales));
    return datosPersonales;
  }

  private AnimalPerdido animalPerdido(Request request) throws ServletException, SQLException, IOException {
    String chapa = request.queryParams("chapita");
    String checkChapita = request.queryMap("checkChapita").value();
    Long chapita = null;

    if (chapa.length() > 0 && checkChapita != null) { //si ingreso el numero de chapita (rescate con chapita)
      chapita = Long.valueOf(this.convertidorDeString(request, "chapita"));
    }
    String descripcion = this.convertidorDeString(request, "descripcion");
    Double posicionX = Double.valueOf(this.convertidorDeString(request, "posicionX"));
    Double posicionY = Double.valueOf(this.convertidorDeString(request, "posicionY"));
    //Por ahora
    List<Foto> fotos = Collections.singletonList(new Foto(this.procesarFoto(request)));
    return new AnimalPerdido(chapita, fotos, descripcion, posicionX, posicionY);
  }

  private Blob procesarFoto(Request request) throws ServletException, IOException, SQLException {
    try (InputStream is = request.raw().getPart("foto").getInputStream()) {
      byte[] bytes = IOUtils.toByteArray(is);
      return new SerialBlob(bytes);
    }
  }
}
