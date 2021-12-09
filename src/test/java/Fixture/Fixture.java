package Fixture;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.*;
import ar.edu.utn.frba.dds.dominio.Rescate.AnimalPerdido;
import ar.edu.utn.frba.dds.dominio.Rescate.Rescate;
import ar.edu.utn.frba.dds.dominio.Rescate.ParticipanteRescate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.sql.rowset.serial.SerialBlob;

public class Fixture {

  public static DatosPersonales datosPersonales() {
    return new DatosPersonales("Diego",
        "Maradona",
        TipoDocumento.DNI,
        "1202121200",
        Collections.singletonList(new Contacto("Diego Maradona",
            "",
            "eldiegote10@gmail.com")),
        LocalDate.now(),
        "Av SiempreViva 123");

  }

  public static ParticipanteRescate rescatistaGenerico() {
    return new ParticipanteRescate(Fixture.datosPersonales());
  }

  public static Contacto contactoGenerico() {
    return new Contacto("Humberto Velez", "1166554433", "humbertovelez@gmail.com");
  }

  public static List<Contacto> contactosGenericos() {
    List<Contacto> contactos = new ArrayList<Contacto>();
    contactos.add(contactoGenerico());
    return contactos;
  }

  public static AnimalPerdido animalPerdidoGenerico() throws SQLException, IOException {
    return new AnimalPerdido(Long.valueOf(1), fotosGenericas(), "Estado generico", 0, 0);
  }

  public static List<Foto> fotosGenericas() throws IOException, SQLException {
    File f = new File("src/test/resources/images/perro.png");
    InputStream is = FileUtils.openInputStream(f);
    byte[] bytes = IOUtils.toByteArray(is);
    Foto foto = new Foto(new SerialBlob(bytes));
    List<Foto> fotosGenericas = new ArrayList<Foto>();
    fotosGenericas.add(foto);
    return fotosGenericas;
  }


  public static Duenio duenioGenerico(Contacto contacto) throws ClassNotFoundException {
    return new Duenio(Fixture.datosPersonales());
  }

  public static Caracteristica caracteristicaGenerica() {
    return new Caracteristica("Es el mejor compañero",TipoCaracteristica.ES_BOOLEANA, null);
  }
}
