package ar.edu.utn.frba.dds.dominio.Repos;

import java.util.ArrayList;
import java.util.List;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;

import ar.edu.utn.frba.dds.dominio.Roles.Usuario;

public class RepoUsuarios implements WithGlobalEntityManager {

  public static RepoUsuarios instancia = new RepoUsuarios();

  public static List<Usuario> usuarioLogueados = new ArrayList<>();

  public List<Usuario> listar() {
    return entityManager()
        .createQuery("from Usuario", Usuario.class)
        .getResultList();
  }

  public Usuario getById(Long id) {
    return entityManager().find(Usuario.class, id);
  }

  public void agregar(Usuario usuario) {
    if (usuario.getId() == null) {
      entityManager().persist(usuario);
    } else {
      entityManager().merge(usuario);
    }
  }

  public Usuario buscarPorUsuarioYContrasenia(String username, String password) {
    return listar().stream().filter(u -> u.getPassword().equals(password) && u.getUsername().equals(username))
        .findFirst().get();
  }

}
