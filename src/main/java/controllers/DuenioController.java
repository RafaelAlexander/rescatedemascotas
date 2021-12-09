package controllers;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.DatosPersonales;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Duenio;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.Repos.RepoDuenio;
import ar.edu.utn.frba.dds.dominio.Repos.RepoUsuarios;
import funciones.NotificacionUsuario;
import funciones.SesionUsuario;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.*;

public class DuenioController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public ModelAndView getFormularioDuenio(Request request, Response response) {
    Usuario usuario = SesionUsuario.estaLogueado(request);
    if (usuario == null) {
      response.redirect("/login");
      return null;
    }
    if (usuario.getCuentaDeDuenio() != null) {
      response.redirect("/");
      return null;
    }
    Map<String, Object> model = new HashMap<String, Object>();

    SesionUsuario.reconocerRol(usuario, model);
    model.put("sesion", "Estoy logueado");
    model.put("nombreUsuario", usuario.getUsername());

    return new ModelAndView(model, "registroDuenio.html.hbs");
  }


  public ModelAndView registrarDuenio(Request request, Response response) {
    Map<String, Object> viewModel = new HashMap<String, Object>();
    Usuario usuario = SesionUsuario.estaLogueado(request);
    if (usuario == null) {
      response.redirect("/login");
      return null;
    }

    try {
      Contacto contacto1 = new Contacto(
          request.queryParams("nombreContacto1"),
          request.queryParams("telefonoContacto1"),
          request.queryParams("mailContacto1")
      );
      Contacto contacto2 = new Contacto(
          request.queryParams("nombreContacto2"),
          request.queryParams("telefonoContacto2"),
          request.queryParams("mailContacto2")
      );

      List<Contacto> listaDeContactos = new ArrayList<>();
      listaDeContactos.add(contacto1);
      listaDeContactos.add(contacto2);

      DatosPersonales datosPersonales = new DatosPersonales(
          request.queryParams("nombreDuenio"),
          request.queryParams("apellidoDuenio"),
          TipoDocumento.valueOf(request.queryParams("tipoDocument")),
          request.queryParams("nroDocumento"),
          listaDeContactos,
          LocalDate.parse(request.queryParams("fechaNacimiento")),
          request.queryParams("direccion")
      );


      Duenio nuevoDuenio = new Duenio(datosPersonales);
      usuario.setCuentaDeDuenio(nuevoDuenio);

      withTransaction(() -> {
        RepoUsuarios.instancia.agregar(usuario);
      });


      NotificacionUsuario.notificar(viewModel, "success", "Éxito!: ", "Se ha registrado el dueño.");
      response.redirect("/");
    } catch (Exception e) {
      NotificacionUsuario.notificar(viewModel, "danger", "Error: ", e.getMessage());
    }
    SesionUsuario.reconocerRol(usuario, viewModel);
    viewModel.put("sesion", "Estoy logueado");
    viewModel.put("nombreUsuario", usuario.getUsername());
    return new ModelAndView(viewModel, "registroDuenio.html.hbs");
  }

}