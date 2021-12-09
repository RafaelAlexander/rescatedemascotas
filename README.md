# java-base-project

- Pagina de heroku: https://centroderescate.herokuapp.com/

Esta es una plantilla de proyecto diseñada para:

- Java 8. :warning: Si bien el proyecto no lo limita explícitamente, el comando `mvn verify` no funcionará con versiones mas modernas de Java.
- JUnit 5. :warning: La versión 5 de JUnit es la más nueva del framework y presenta algunas diferencias respecto a la versión "clásica" (JUnit 4). Para mayores detalles, ver:
  - [Apunte de herramientas](https://docs.google.com/document/d/1VYBey56M0UU6C0689hAClAvF9ILE6E7nKIuOqrRJnWQ/edit#heading=h.dnwhvummp994)
  - [Entrada de Blog (en inglés)](https://www.baeldung.com/junit-5-migration)
  - [Entrada de Blog (en español)](https://www.paradigmadigital.com/dev/nos-espera-junit-5/)
- Maven 3.3 o superior

# Ejecutar tests

```
mvn test
```

# Validar el proyecto de forma exahustiva

```
mvn clean verify
```

Este comando hará lo siguiente:

1.  Ejecutará los tests
2.  Validará las convenciones de formato mediante checkstyle
3.  Detectará la presencia de (ciertos) code smells
4.  Validará la cobertura del proyecto

# Entrega del proyecto

Para entregar el proyecto, crear un tag llamado `entrega-final`. Es importante que antes de realizarlo se corra la validación
explicada en el punto anterior. Se recomienda hacerlo de la siguiente forma:

```
mvn clean verify && git tag entrega-final && git push origin HEAD --tags
```

# Configuración del IDE (IntelliJ)

1.  Tabular con dos espacios: ![Screenshot_2021-04-09_18-23-26](https://user-images.githubusercontent.com/677436/114242543-73e1fe00-9961-11eb-9a61-7e34be9fb8de.png)
2.  Instalar y configurar Checkstyle:
    1. Instalar el plugin https://plugins.jetbrains.com/plugin/1065-checkstyle-idea:
    2. Configurarlo activando los Checks de Google: ![Screenshot_2021-04-09_18-16-13](https://user-images.githubusercontent.com/677436/114242548-75132b00-9961-11eb-972e-28e6e1412979.png)
3.  Usar fin de linea unix
    1. En **Settings/Preferences**, ir a a **Editor | Code Style**.
    2. En la lista **Line separator**, seleccionar `Unix and OS X (\n)`.
       ![Screenshot 2021-04-10 03-49-00](https://user-images.githubusercontent.com/11875266/114260872-c6490c00-99ad-11eb-838f-022acc1903f4.png)

# Configuracion de variables de entorno

Para que se ejecuten correctamente los tests de Notificación se deben agregar las siguientes variables de entorno de usuario:

- **DDS2021_EMAILSERVICE_USER**
- **DDS2021_EMAILSERVICE_PASS**
- **DDS2021_SMSSERVICE_ACCOUNTSID**
- **DDS2021_SMSSERVICE_AUTHTOKEN**

> Se pasarán los valores de las variables de forma privada para evitar que queden registradas en el repositorio

## Procedimiento para Windows 10

1. Ir a: Sistema > Configuracion avanzada del Sistema > Variables de Entorno
2. En _variables de usuario_ agregar todas las variables indicadas anteriormente.
3. [IMPORTANTE] Reiniciar el IDE en caso de tenerlo abierto al momento de configurar las variables

## Procedimiento para Linux

1. Editar el profile del usuario con el que se esté ejecutando el IDE:

```
vi ~/.bash_profile
```

2. Agregar los export de las variables a agregar en el profile

```
export DDS2021_EMAILSERVICE_USER="ejemplo@gmail.com"
export ...
```

3. Guardar y cerrar

```
:wq
```

4. Reiniciar el IDE
