package controllers;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.*;
import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import ar.edu.utn.frba.dds.dominio.Repos.RepoUsuarios;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
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
import java.util.*;

public class MascotaController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public ModelAndView getMisMascotas(Request request, Response response) {
    Map<String, Object> viewModel = new HashMap<String, Object>();
    Usuario usuario = SesionUsuario.estaLogueado(request);
    if (usuario == null) {
      response.redirect("/login");
      return null;
    }
    Duenio duenio = usuario.getCuentaDeDuenio();
    if (duenio == null) {
      NotificacionUsuario.notificar(viewModel, "warning", "Atencion!: ", "Usted debe ir a registrarse como dueño");
      return new ModelAndView(viewModel, "registroDuenio.html.hbs");
    }
    List<Mascota> mascotas = duenio.getMascotas();
    viewModel.put("sesion", "Estoy logueado");
    viewModel.put("nombreUsuario", SesionUsuario.estaLogueado(request).getUsername());
    viewModel.put("mascotas", mascotas);
    SesionUsuario.reconocerRol(usuario, viewModel);
    return new ModelAndView(viewModel, "misMascotas.html.hbs");
  }

  public ModelAndView getFormularioMascota(Request request, Response response) {
    Usuario usuario = SesionUsuario.estaLogueado(request);
    if (usuario == null) {
      response.redirect("/login");
      return null;
    }

    if (usuario.getCuentaDeDuenio() == null) {
      response.redirect("/registrarDuenio");
      return null;
    }

    Map<String, Object> model = new HashMap<String, Object>();
    List<Caracteristica> caracteristicas = RepoCaracteristicas.getInstance().getCaracteristicas();

    model.put("sesion", "Estoy logueado");
    model.put("nombreUsuario", SesionUsuario.estaLogueado(request).getUsername());
    model.put("caracteristicasDisponibles", caracteristicas);
    SesionUsuario.reconocerRol(usuario, model);
    return new ModelAndView(model, "registroMascota.html.hbs");
  }

  public ModelAndView registrarMascota(Request request, Response response) {
    Map<String, Object> viewModel = new HashMap<>();
    Usuario usuario = SesionUsuario.estaLogueado(request);

    if (usuario == null) {
      response.redirect("/login");
      return null;
    }
    if (usuario.getCuentaDeDuenio() == null) {
      response.redirect("/registrarDuenio");
    }


    try {
      request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
      List<Caracteristica> caracteristicas = RepoCaracteristicas.getInstance().getCaracteristicas();
      ArrayList<CaracteristicaMascota> caracteristicasParaMascota = new ArrayList<>();

      for (int i = 0; i < caracteristicas.size(); i++) {
        Caracteristica caracteristicaActual = caracteristicas.get(i);
        caracteristicasParaMascota.add(new CaracteristicaMascota(caracteristicaActual.getDescripcion(), this.convertidorDeString(request, caracteristicaActual.getDescripcion())));
      }


      Mascota mascotaAAgregar = new Mascota(
          TipoMascota.valueOf(this.convertidorDeString(request, "tipoMascota")),
          this.convertidorDeString(request, "nombreMascota"),
          this.convertidorDeString(request, "apodoMascota"),
          Integer.valueOf(this.convertidorDeString(request, "edadMascota")),
          TipoSexo.valueOf(this.convertidorDeString(request, "sexoMascota")),
          this.convertidorDeString(request, "descripcionMascota"),
          Collections.singletonList(new Foto(this.procesarFoto(request))),
          caracteristicasParaMascota,
          usuario.getCuentaDeDuenio()
      );

      withTransaction(() -> {
        RepoUsuarios.instancia.agregar(usuario);
      });
      NotificacionUsuario.notificar(viewModel, "success", "Éxito!: ", "Se ha registrado la mascota.");
    } catch (Exception e) {
      NotificacionUsuario.notificar(viewModel, "danger", "Error: ", e.getMessage());
    }
    viewModel.put("sesion", "Estoy logueado");
    viewModel.put("nombreUsuario", usuario.getUsername());
    SesionUsuario.reconocerRol(usuario, viewModel);
    return new ModelAndView(viewModel, "registroMascota.html.hbs");
  }

  private Blob procesarFoto(Request request) throws ServletException, IOException, SQLException {
    try (InputStream is = request.raw().getPart("fotoMascota").getInputStream()) {
      byte[] bytes = IOUtils.toByteArray(is);
      return new SerialBlob(bytes);
    }
  }

  private String convertidorDeString(Request request, String nombre) throws ServletException, IOException {
    return IOUtils.toString(request.raw().getPart(nombre).getInputStream(), StandardCharsets.UTF_8);
  }
}
