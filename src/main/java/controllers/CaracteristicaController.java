package controllers;

import ar.edu.utn.frba.dds.dominio.Repos.RepoCaracteristicas;
import funciones.NotificacionUsuario;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Caracteristica;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoCaracteristica;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import funciones.SesionUsuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaracteristicaController implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public ModelAndView getFormularioCaracteristica(Request request, Response response) {
    Usuario usuario = SesionUsuario.estaLogueado(request);
    if (usuario == null) {
      response.redirect("/login");
      return null;
    }
    if (!usuario.esAdmin()) {
      response.redirect("/");
      return null;
    }

    Map<String, Object> model = new HashMap<String, Object>();

   SesionUsuario.reconocerRol(usuario,model);
    model.put("sesion", "Estoy logueado");
    model.put("nombreUsuario", SesionUsuario.estaLogueado(request).getUsername());

    return new ModelAndView(model, "agregarCaracteristica.html.hbs");
  }

  public ModelAndView agregarCaracteristica(Request request, Response response) {

    Map<String, Object> viewModel = new HashMap<String, Object>();
    Usuario usuario = SesionUsuario.estaLogueado(request);

    if (usuario == null) {
      response.redirect("/login");
      return null;
    }
    if (!usuario.esAdmin()) {
      response.redirect("/");
      return null;
    }

    String nuevaCaracteristica = request.queryParams("nombreCaracteristica");
    String tipoCaracteristicaSeleccionada = request.queryParams("tipoCaracteristica");

    List<String> listaValores = new ArrayList<>();

    TipoCaracteristica tipoCaracteristica;
    if (tipoCaracteristicaSeleccionada.equals("B")) {
      tipoCaracteristica = TipoCaracteristica.ES_BOOLEANA;
    } else {
      tipoCaracteristica = TipoCaracteristica.CON_VALORES;
      listaValores = getListaValores(request);
    }

    try {
      Caracteristica caracteristica = new Caracteristica(nuevaCaracteristica, tipoCaracteristica, listaValores);
      withTransaction(() -> usuario.getCuentaAdministrador().agregarCaracteristicaARepositorio(caracteristica));
      NotificacionUsuario.notificar(viewModel, "success", "Éxito!: ", "Se ha agregado la característica.");
    } catch (Exception exception) {
      NotificacionUsuario.notificar(viewModel, "danger", "Error ", exception.getMessage());
    }
    SesionUsuario.reconocerRol(usuario,viewModel);
    return new ModelAndView(viewModel, "agregarCaracteristica.html.hbs");
  }

  private List<String> getListaValores(Request request) {
    List<String> valoresCaracteristicas = new ArrayList<>();
    int numeroCaracteristicas = Integer.valueOf(request.queryParams("cantNumeros"));
    String caracteristicaAAgregar = null;
    int contador = 1;
    for (int i = 0; i < numeroCaracteristicas; i++) {
      caracteristicaAAgregar = request.queryParams("caracteristica" + contador);
      valoresCaracteristicas.add(caracteristicaAAgregar);
      contador++;
    }
    return valoresCaracteristicas;
  }

}
