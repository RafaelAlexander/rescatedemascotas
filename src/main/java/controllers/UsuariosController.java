package controllers;

import funciones.NotificacionUsuario;
import funciones.SesionUsuario;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import ar.edu.utn.frba.dds.dominio.Repos.RepoUsuarios;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuariosController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public ModelAndView getFormularioLogin(Request request, Response response) {
    return new ModelAndView(null, "login.html.hbs");
  }

  public ModelAndView iniciarSesion(Request request, Response response) {

    Map<String, Object> viewModel = new HashMap<String, Object>();
    String username = request.queryParams("username");
    String password = request.queryParams("password");
    Usuario usuario = null;

    try {
      List<Usuario> usuarioList = RepoUsuarios.instancia.listar();
      usuario = RepoUsuarios.instancia.listar().stream().filter(u ->
              BCrypt.checkpw(password, u.getPassword()) &&
                  u.getUsername().equals(username))
          .findFirst().get();
      try {
        if (usuario.getCuentaDeDuenio() == null && !usuario.esAdmin()) {
          NotificacionUsuario.notificar(viewModel, "warning", "Atencion!: ", "Usted debe ir a registrarse como dueño");
          return new ModelAndView(viewModel, "registroDuenio.html.hbs");
        }
      } catch (Exception e) {
        NotificacionUsuario.notificar(viewModel, "danger", "Error: ", e.getMessage());
        return new ModelAndView(viewModel, "login.html.hbs");
      }
    } catch (Exception e) {
      NotificacionUsuario.notificar(viewModel, "danger", "Error: ", "Se debe registrar como usuario.");
      return new ModelAndView(viewModel, "login.html.hbs");
    }


    request.session().attribute("idUsuario", usuario.getId());
    if (usuario.esAdmin()) {
      System.out.println(usuario.getUsername() + " es usuario admin");
    } else {
      System.out.println(usuario.getUsername() + " es usuario normal");
    }
    response.redirect("/");

    return null;

  }


  public ModelAndView getFormularioRegistrarUsuario(Request request, Response response) {
    return new ModelAndView(null, "registrarUsuario.html.hbs");
  }

  public ModelAndView registrarUsuario(Request request, Response response) {
    Map<String, Object> viewModel = new HashMap<String, Object>();
    String username = request.queryParams("username");
    String password = request.queryParams("password");
    Usuario usuario=new Usuario();
    try {
      usuario = RepoUsuarios.instancia.listar().stream().filter(u -> u.getPassword().equals(password) && u.getUsername().equals(username)).findFirst().get();
      NotificacionUsuario.notificar(viewModel, "warning", "Advertencia: ", "El usuario ya se encuentra registrado.");
      return new ModelAndView(viewModel, "login.html.hbs");
    } catch (Exception e) {
      Usuario usr = new Usuario(username, password);
      withTransaction(() -> {
        RepoUsuarios.instancia.agregar(usr);
      });
      request.session().attribute("idUsuario", usr.getId());
      NotificacionUsuario.notificar(viewModel, "success", "Éxito!: ", "Se ha registrado el usuario.");
      SesionUsuario.reconocerRol(usuario, viewModel);
      return new ModelAndView(viewModel, "home.html.hbs");
    }
  }

  private Usuario getUsuarioLogueado(Request request) {
    Long id = request.session().attribute("idUsuario");
    if (id == null) {
      return null;
    }
    return RepoUsuarios.instancia.getById(id);
  }

  private boolean estaLogueado(Request request) {
    return this.getUsuarioLogueado(request) != null;
  }

  public Void cerrarSesion(Request req, Response res) {

    req.session().removeAttribute("idUsuario");

    res.redirect("/");
    return null;
  }

}
