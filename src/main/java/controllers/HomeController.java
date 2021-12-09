package controllers;

import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import funciones.SesionUsuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class HomeController {

  public ModelAndView getHome(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    Usuario usuario = SesionUsuario.estaLogueado(request);
    if (usuario != null) {
      modelo.put("sesion", "Estoy logueado");
      modelo.put("nombreUsuario", usuario.getUsername());
      SesionUsuario.reconocerRol(usuario, modelo);
    }
    return new ModelAndView(modelo, "home.html.hbs");
  }

}
