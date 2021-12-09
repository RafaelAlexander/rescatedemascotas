package main;

import static spark.Spark.after;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import controllers.CaracteristicaController;
import controllers.DuenioController;
import controllers.HomeController;
import controllers.MascotaController;
import controllers.RescateController;
import controllers.UsuariosController;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Routes {

  public static void main(String[] args) {

    System.out.println("Corriendo bootstrap...");
    new Bootstrap().run();

    System.out.println("Iniciando servidor...");
    Spark.port(9090);
    Spark.staticFileLocation("/public");

    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();

    HomeController homeController = new HomeController();
    UsuariosController usuariosController = new UsuariosController();
    DuenioController duenioController = new DuenioController();
    RescateController rescateController = new RescateController();
    CaracteristicaController caracteristicaController = new CaracteristicaController();
    MascotaController mascotaController = new MascotaController();

    Spark.get("/", (request, response) -> homeController.getHome(request, response), engine);

    Spark.get("/login", (request, response) -> usuariosController.getFormularioLogin(request, response), engine);
    Spark.post("/login", (request, response) -> usuariosController.iniciarSesion(request, response), engine);
    Spark.get("/logout", (request, response) -> usuariosController.cerrarSesion(request, response));

    Spark.get("/registrarUsuario", (request, response) -> usuariosController.getFormularioRegistrarUsuario(request, response), engine);
    Spark.post("/registrarUsuario", (request, response) -> usuariosController.registrarUsuario(request, response), engine);

    Spark.get("/registrarDuenio", (request, response) -> duenioController.getFormularioDuenio(request, response), engine);
    Spark.post("/registrarDuenio", (request, response) -> duenioController.registrarDuenio(request, response), engine);

    Spark.get("/misMascotas", (request, response) -> mascotaController.getMisMascotas(request, response), engine);

    Spark.get("/registrarMascota", (request, response) -> mascotaController.getFormularioMascota(request, response), engine);
    Spark.post("/registrarMascota", (request, response) -> mascotaController.registrarMascota(request, response), engine);

    Spark.get("/rescate", (request, response) -> rescateController.getFormularioRescate(request, response), engine);
    Spark.post("/rescate", (request, response) -> rescateController.guardarRescate(request, response), engine);

    Spark.get("/agregarCaracteristica", (request, response) -> caracteristicaController.getFormularioCaracteristica(request, response), engine);
    Spark.post("/agregarCaracteristica", (request, response) -> caracteristicaController.agregarCaracteristica(request, response), engine);

    System.out.println("Servidor iniciado!");
    after((request, response) -> {
      PerThreadEntityManagers.getEntityManager();
      PerThreadEntityManagers.closeEntityManager();
    });
  }

}
