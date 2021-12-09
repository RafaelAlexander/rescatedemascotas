package ar.edu.utn.frba.dds.dominio.Roles;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Caracteristica;
import ar.edu.utn.frba.dds.dominio.MascotaYDuenio.Duenio;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import org.passay.CharacterRule;
import org.passay.DictionaryRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RepeatCharactersRule;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.UsernameRule;
import org.passay.dictionary.Dictionary;
import org.passay.dictionary.DictionaryBuilder;
import org.springframework.security.crypto.bcrypt.BCrypt;

import ar.edu.utn.frba.dds.dominio.Excepciones.ContraseniaDebilException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;

  private String username; // es el usuario del duenio y admin
  private String password;

  //en vez de que haya una tabla intermedia de roles, donde los usuarios no comparten nada,
  //decidimos que el usuario se relacione directamente con los roles (que son administrador, voluntario o dueño)
  //ademas el tema de tener una tabla de roles, nos permitiria tener un usuario que tiene varias cuentas administrador
  //pero no queremos que pase eso, ya que queremos que el usuario tenga una cuenta administrador o ninguna
  //lo que si puedee tener varias cuentas de voluntario en una asociacion, y esta cuenta le va a pertenecer a un usuario solo

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "usuario_id")
  private List<Voluntario> cuentasVoluntario = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL)
  private Administrador cuentaAdministrador;

  @OneToOne(cascade = CascadeType.ALL)
  private Duenio cuentaDeDuenio;


  public Usuario(String usuario, String contrasenia) {
    if (usuario == null) {
      throw new RuntimeException("Debe ingresar un usuario");
    }
    validarContrasenia(usuario, contrasenia);
    this.username = usuario;
    this.password = hashearContrasenia(contrasenia);
  }

  public void setPassword(String contrasenia) {
    this.password = contrasenia;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  /* cosas internas */

  public void agregarCuentaDeDuenioNueva(Duenio cuentaNueva) {
    cuentaDeDuenio = cuentaNueva;
  }

  public void convertirseEnAdmin() {
    cuentaAdministrador = new Administrador();
  }

  public boolean esAdmin() {
    return cuentaAdministrador != null;
  }

  private void agregarCuentaVoluntario(Asociacion asociacion) {
    Voluntario cuentaNueva = new Voluntario(asociacion);
    cuentasVoluntario.add(cuentaNueva);
  }


  private void validarContrasenia(String nombreUsuario, String contrasenia) {
    PasswordData passwordData = new PasswordData();
    passwordData.setUsername(nombreUsuario);
    passwordData.setPassword(contrasenia);

    PasswordValidator passwordValidator = getPasswordValidator();

    RuleResult validate = passwordValidator.validate(passwordData);
    if (!validate.isValid()) {
      throw new ContraseniaDebilException(definirMensajeDelError(validate));
    }
  }

  private String definirMensajeDelError(RuleResult validate) {
    RuleResultDetail ruleResultDetail = validate.getDetails().get(0);
    return String.valueOf(getDiccionarioDeErrores().get(ruleResultDetail.getErrorCode()));
  }

  private HashMap<String, String> getDiccionarioDeErrores() {
    HashMap<String, String> DiccionarioDeErrores = new HashMap<>();
    DiccionarioDeErrores.put("ILLEGAL_WORD", "La contraseña ingresada es muy facil");
    DiccionarioDeErrores.put("TOO_SHORT", "La contraseña debe tener al menos 8 caracteres");
    DiccionarioDeErrores.put("TOO_LONG", "La contraseña puede tener 64 caracteres como maximo");
    DiccionarioDeErrores.put("ILLEGAL_USERNAME", "La contraseña debe ser distinta al nombre de usuario");
    DiccionarioDeErrores.put("ILLEGAL_ALPHABETICAL_SEQUENCE", "La contraseña no puede contener secuencias alfabeticas");
    DiccionarioDeErrores.put("ILLEGAL_NUMERICAL_SEQUENCE", "La contraseña no puede contener secuencias numericas");
    DiccionarioDeErrores.put("ILLEGAL_REPEATED_CHARS", "La contraseña contiene una repetición de caracteres");
    DiccionarioDeErrores.put("INSUFFICIENT_UPPERCASE", "La contraseña debe contener uno o más caracteres en mayúscula");
    DiccionarioDeErrores.put("INSUFFICIENT_LOWERCASE", "La contraseña debe contener uno o más caracteres en minúscula");
    return DiccionarioDeErrores;
  }

  private PasswordValidator getPasswordValidator() {
    return new PasswordValidator(
        reglaConClavesBaneadas(),
        new LengthRule(8, 64),
        new UsernameRule(),
        new IllegalSequenceRule(EnglishSequenceData.Alphabetical),
        new IllegalSequenceRule(EnglishSequenceData.Numerical),
        new RepeatCharactersRule(),
        new CharacterRule(EnglishCharacterData.UpperCase),
        new CharacterRule(EnglishCharacterData.LowerCase)
    );
  }

  private DictionaryRule reglaConClavesBaneadas() throws RuntimeException {
    Class cls;
	try {
		cls = Class.forName("ar.edu.utn.frba.dds.dominio.Roles.Usuario");
	} catch (ClassNotFoundException e) {
		throw new RuntimeException("Error con claves Baneadas");
	}
    ClassLoader cLoader = cls.getClassLoader();
    InputStream inputStream = cLoader.getResourceAsStream("10k-most-common.txt");
    DictionaryBuilder dictionaryBuilder = new DictionaryBuilder();
    Dictionary diccionarioContraseniasFaciles = dictionaryBuilder.addReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).build();
    return new DictionaryRule(diccionarioContraseniasFaciles);
  }

  private String hashearContrasenia(String contraseniaPlana) {
    return BCrypt.hashpw(contraseniaPlana, BCrypt.gensalt());
  }

  private boolean contraseniaEsCorrecta(String contraseniaCandidata) {
    return BCrypt.checkpw(contraseniaCandidata, this.password);
  }

  public boolean autenticar(String nombreUsuario, String contrasenia) {
    return this.username.equals(nombreUsuario) &&
        contraseniaEsCorrecta(contrasenia);
  }

  public Long getId() {
    return id;
  }

  public String getPassword() {
	return password;
  }

  public String getUsername() {
	return username;
  }

}