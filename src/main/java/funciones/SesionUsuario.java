package funciones;

import ar.edu.utn.frba.dds.dominio.Repos.RepoUsuarios;
import ar.edu.utn.frba.dds.dominio.Roles.Usuario;
import spark.Request;

import java.util.Map;

public final class SesionUsuario {

  private SesionUsuario() {

  }

  public static Usuario estaLogueado(Request request) {
    Long id = request.session().attribute("idUsuario");
    if (SesionUsuario.verificarVacio(id))
      return null;
    Usuario usuario = RepoUsuarios.instancia.getById(id);
    if (SesionUsuario.verificarVacio(usuario)) {
      return null;
    }
    return usuario;
  }

  public static void reconocerRol(Usuario usuario, Map<String, Object> modelo) {
    if (usuario.getCuentaDeDuenio() != null) {
      modelo.put("duenio", "si");
    }
    if (usuario.getCuentaAdministrador() != null) {
      modelo.put("admin", "si");
    }
  }

  private static boolean verificarVacio(Object object) {
    if (object == null) {
      return true;
    }
    return false;
  }
}
