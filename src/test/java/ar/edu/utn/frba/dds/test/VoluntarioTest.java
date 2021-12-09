package ar.edu.utn.frba.dds.test;

import Fixture.Fixture;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Contacto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Foto;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.TipoDocumento;
import ar.edu.utn.frba.dds.dominio.Repos.RepoAsociaciones;
import ar.edu.utn.frba.dds.dominio.Rescate.*;
import ar.edu.utn.frba.dds.dominio.Roles.Voluntario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VoluntarioTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  void initFileSystem() {
    RepoAsociaciones asociaciones = mock(RepoAsociaciones.class);
  }

  @BeforeEach
  public void antes() {
    this.beginTransaction();
  }

  @AfterEach
  public void despues() {
    this.rollbackTransaction();
  }

  @Test
  public void noSeValidaLaPublicacionSiLaAsociacionDelVoluntarioActualNoEsLaMasCercana() throws SQLException, IOException {

    Asociacion nuevaAsociacion = new Asociacion(2, 2);
    Voluntario voluntario = new Voluntario(nuevaAsociacion);

    Contacto contactoRecatista = new Contacto("Oscar Gonzalez", "11465789", "oscarg@gmail.com");
    List<Contacto> contactosDeRescatista = new ArrayList<>();
    contactosDeRescatista.add(contactoRecatista);
    ParticipanteRescate resctista = Fixture.rescatistaGenerico();
    Foto nuevaFoto = Fixture.fotosGenericas().stream().findFirst().orElse(null);
    List<Foto> listaDeFotos = new ArrayList<>();
    listaDeFotos.add(nuevaFoto);
    AnimalPerdido mascotaPerdida = new AnimalPerdido(null, listaDeFotos, "Mal", 0, 0);

    RescateSinChapita rescate = new RescateSinChapita(resctista, mascotaPerdida);

    Assertions.assertThrows(RuntimeException.class, () -> voluntario.aprobarPublicacion(rescate));

  }

  @Test
  public void seValidaLaPublicacionSiLaAsociacionDelVoluntarioActualEsLaMasCercana() throws SQLException, IOException {

    RepoAsociaciones.getInstance().agregarEntity(entityManager());

    Asociacion nuevaAsociacion = new Asociacion(1, 0);
    Asociacion nuevaAsociacion2 = new Asociacion(3, 3);
    RepoAsociaciones.getInstance().agregarAsociacion(nuevaAsociacion);
    RepoAsociaciones.getInstance().agregarAsociacion(nuevaAsociacion2);

    Voluntario voluntario = new Voluntario(nuevaAsociacion);

    Contacto contactoRecatista = new Contacto("Oscar Gonzalez", "11465789", "oscarg@gmail.com");
    List<Contacto> contactosDeRescatista = new ArrayList<>();
    contactosDeRescatista.add(contactoRecatista);
    ParticipanteRescate resctista = Fixture.rescatistaGenerico();
    Foto nuevaFoto = Fixture.fotosGenericas().stream().findFirst().orElse(null);
    List<Foto> listaDeFotos = new ArrayList<>();
    listaDeFotos.add(nuevaFoto);

    AnimalPerdido mascotaPerdida = new AnimalPerdido(null, listaDeFotos, "Mal", 0, 0);

    RescateSinChapita rescate = new RescateSinChapita(resctista, mascotaPerdida);

    voluntario.aprobarPublicacion(rescate);

    //una vez que se validen los datos y de toda la data  correcta, deberia agregarse la publicacion a la asociacion
    Assertions.assertEquals(nuevaAsociacion.cantidadPublicacionesAprobadas(), 1);

  }


}