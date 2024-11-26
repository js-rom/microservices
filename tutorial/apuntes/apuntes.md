# Curso de Microservicios Escuela IT

# Clase 1. Arquitectura de microservicios y Rest API 

## Índice
- ...
- ...

## ...

## Lombok

## Crear un controlador con un CRUD
1. Creamos una clase y le ponemos la anotación @RestController para exponer esta clase con un endpoint

```java
package es.edu.escuela_it.Miroservices.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersControllerRest {

}
```

2. GET{param}. Creamos un servicio para recuperar un usuario por id, para ello basta simplemente 
con ponerle la anotración @GetMapping al método que queremos exponer como servio.

```java
@RestController
public class UsersControllerRest {

	@GetMapping("/users/{id}")
	public UserDTO getUserById(@PathVariable int id) {
		
		System.out.println("Recovery user by id");
		
		UserDTO userDTO = new UserDTO(1, "Jesús");
		userDTO.setAge(33);
		userDTO.setLastName("Romero Vidal");
		
		return userDTO;
	}
}
```
puntualizar que de momento solo hemos hardcodeado el usuario que estamos devolviendo en la llamada a este servicio.
A la anotacion @GetMapping le pasamos como parámetro la ruta donde se expondrá este serrvicio. Es buena práctica ponerle a la ruta el mismo nombre que la clase del modelo, "users". Después de la ruta le hemos pasado un **URI Template Variable** "{id}", de esta forma es como indicamos que será un parámetro variable de la ruta.

Para recibir esta **URI Template Variable** es necesario utilizar la anotación @PathVariable en los argumentos del método. Si el nombre del parámetro de la ruta y el argumento del método coinciden basta con poner esta anotación simple, de otra forma tendriamos que espcificar el mapeo en la anotación tla que así:

```java
@RestController
public class UsersControllerRest {

	@GetMapping("/users/{id}")
	public UserDTO getUserById(@PathVariable("id") int userId) {
		
		System.out.println("Recovery user by id");
		
		UserDTO userDTO = new UserDTO(1, "Jesús");
		userDTO.setAge(33);
		userDTO.setLastName("Romero Vidal");
		
		return userDTO;
	}
}
```
3. Spring internamente convierte el objeto devuelto a JSOn haciendo uso de Jackson, por lo que no tenemos que procuparnor
por parsear entre Java y JSON. Esta es la salida del navegador ya en formato JSON:

![alt text](./docs/image.png)


4. GET. Creamos un segundo servicio para devolver todos los usuarios. Observad como con las anotaciones también hay sobreescritura, pues estamos creando don GET poro sus parámetros de ruta son diferentes ("/users/{id}" != "/users") por lo que tienen tipo diferente y no colisionan

```java
@RestController
public class UsersControllerRest {

	@GetMapping("/users/{id}")
	public UserDTO getUserById(@PathVariable int id) {
		
		System.out.println("Recovery user by id");
		
		UserDTO userDTO = new UserDTO(1, "Jesús");
		userDTO.setAge(33);
		userDTO.setLastName("Romero Vidal");
		
		return userDTO;
	}
	
	@GetMapping("/users")
	public List<UserDTO> listAllUsers() {
		List<UserDTO> list = List.of(new UserDTO(1, "Jesús"), new UserDTO(2, "Miguel"), new UserDTO(3, "Álvaro"));
		return list;
	}
}
```
5. POST. Creamos un recuros con el verbo POST. También es buena práctica ponerlo en la misma ruta que los métodos get "/users", que es el nombre del modelo que estamos alterando

```java
	@PostMapping("/users")
	public String createUser(@RequestBody UserDTO userDTO) {
		
		System.out.println("Creating user" + userDTO.getId());
		
		return "http://localhost:8080/users/" + userDTO.getId();
	}
```

Es buena práctica devolver la uri en la que se acaba de crear el recurso.
Para crear el usuario necesitamos que nos lo envíen desde el lado del cliente suministrandolo en el payLoad de la llamada, el body.
Para recibir por parámetro un body de una peticion hhtp necesitamos indicarlo con la anotación @RequestBody. el body llega como un JSON pero aquí Spring hace la operacion reversa, parsea el JSON al objeto UserDTO de Java.

6. Comenzamos a usar Postman para poder enviar un body a los métodos que lo necesiten como el POST.
7. PUT. modificamos un recurso con el verbo PUT. PUT se utiliza cuando queramos modificar un recurso en su totalidad. Cuando hacemos un update es bueno devolver el recurso modificado.

```java
	@PutMapping("/users")
	public UserDTO updateUser(UserDTO userDTO) {
		
		System.out.println("Updating data");
		
		return userDTO;
	}
```

8. DELETE. Eliminar un usuario por id.

```java
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		
		System.out.println("Delete user by id");
		
	}
```

9. PATCH. actualización parcial de ciertos atributos.

```java
    @PatchMapping(value = "/users/{id}")
    public UserDTO updateAge(Map<String,Object> attributes, @PathVariable("id") String id) {
        UserDTO userDTO = new UserDTO();

        //Logica aquí

        return userDTO;
}
```
10. GET con QUERY FILTER. Filtrar una consultaget con parámetros.

```java
    @GetMapping("/users")
    public List<UserDTO> listAllUsers( @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String lastName,
                                    @RequestParam(required = false) Integer age) {

        List<UserDTO> list = List.of(new UserDTO(1, "Rafael"), new UserDTO(2, "Miguel"), new UserDTO(3, "Alvaro"));

        //lógica de filtrado:
        list = list.stream().filter(u -> u.getName().contains(name)).collect(Collectors.toList());
        return list;
    }
```
esto parámetros llegaria mediante la url por medio de query params:
![alt text](./docs/image2.png)




# Clase 2. Capa controller, Swagger y HATEOAS.
## Índice.
1. Cómo cambiar el puerto donde escucha la aplicación.
2. Cómo cambiar la URI base (ej: localhost:8080).
3. Cómo unificar rutas por controller.
4. Cómo configurar los códigos de respuesta.
5. Anidación de recursos.
5. Buenas prácticas.
6. Externalización de parámetros.
7. Documentación REST con Swagger
8. Navegación REST con HATEOAS.

## Cómo cambiar el puerto donde escucha la aplicación
Nos dirigimos al archivo application.properties e introducimos una linea especificando el puerto donde escucha por defecto:

```
server.port=8081
```

## Cómo cambiar la URI base (ej: localhost:8080)
Nos dirigimos al archivo application.properties e introducimos una linea especificando el URI base para todos los microservicios.
importante que el base URI contenga la organización/api/version/conjuntoDeEndpoints

```
server.servlet.context-path=/escuelait/api/v2/microservices
```
incluimos la nueva base URI en las variables de entorno de postman y comprobamos que sigue funcionando correctamente:

![alt text](./docs/image3.png)

## Cómo unificar rutas por controller

ponemos la siguiente anotación a la clase controller y ya no necesitaremos indicarla en cada método:

@RequestMapping("/users")

```java
@RestController
@RequestMapping("/users")
public class UsersControllerRest {

	@GetMapping("/{id}")
	public UserDTO getUserById(@PathVariable int id) {
		
		System.out.println("Recovery user by id");
		
		UserDTO userDTO = new UserDTO(1, "Jesús");
		userDTO.setAge(33);
		userDTO.setLastName("Romero Vidal");
		
		return userDTO;
	}
	
	@GetMapping
	public List<UserDTO> listAllUsers() {
		List<UserDTO> list = List.of(new UserDTO(1, "Jesús"), new UserDTO(2, "Miguel"), new UserDTO(3, "Álvaro"));
		return list;
	}
	
	@PostMapping
	public String createUser(@RequestBody UserDTO userDTO) {
		
		System.out.println("Creating user" + userDTO.getId());
		
		return "http://localhost:8080/users/" + userDTO.getId();
	}
	
	@PutMapping
	public UserDTO updateUser(@RequestBody UserDTO userDTO) {
		
		System.out.println("Updating data");
		
		return userDTO;
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable int id) {
		
		System.out.println("Delete user by id");
		
	}
	
}
```

## Cómo configurar los códigos de respuesta.

Para configurar los códigos de respuesta utilizaremos en cada método el generic ResponseEntity \<T> que envuelve al objeto que devulve el método. al retornar el ResponseEntity envolviendo al objeto devolvemos el objeto mas información adicional.

Hay varias formas de devolver el código de error, de momento nos centramos en una de ellas que es mediante un método estático de la clase ResponseEntity

### Respuesta OK:
#### GET --> 200 ok

antes:

```java
	@GetMapping("/{id}")
	public UserDTO getUserById(@PathVariable int id) {
		
		System.out.println("Recovery user by id");
		
		UserDTO userDTO = new UserDTO(1, "Jesús");
		userDTO.setAge(33);
		userDTO.setLastName("Romero Vidal");
		
		return userDTO;
	}
```
Después:

```java
@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
		
		System.out.println("Recovery user by id");
		
		UserDTO userDTO = new UserDTO(1, "Jesús");
		userDTO.setAge(33);
		userDTO.setLastName("Romero Vidal");
		
		return ResponseEntity.ok(userDTO);
	}
```

otro ejemplo coin GET:

```java
	@GetMapping
	public ResponseEntity<List<UserDTO>> listAllUsers() {
		List<UserDTO> list = List.of(new UserDTO(1, "Jesús"), new UserDTO(2, "Miguel"), new UserDTO(3, "Álvaro"));
		return ResponseEntity.ok(list);
	}
```
#### POST -> 201 created
Este es algo más complajo. es buena práctica devolver la ruta donde se puede encontrar el recurso creado pero no es buena práctica hacerlo hardcodeado como lo tenemos ahora. Por lo que tenemos que usar la clase ServletUriComponentsBuilder que nos permite crear links basados en la httpsServerletRequest actual

antes:
```java
	@PostMapping
	public String createUser(@RequestBody UserDTO userDTO) {
		
		System.out.println("Creating user" + userDTO.getId());
		
		return "http://localhost:8080/users/" + userDTO.getId();
	}
```

después:
```java
	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
		
		System.out.println("Creating user" + userDTO.getId());
		
		URI location = ServletUriComponentsBuilder.
				fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(userDTO.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
```
#### PUT -> 200 ok
```java
	@PutMapping
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
		
		System.out.println("Updating data");
		
		return ResponseEntity.ok(userDTO);
	}
```
#### DELETE -> 200 ok
aquí devolvemos un paremetrizado con null porque el método no devolvía nada, es void:
antes:
```java
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable int id) {
		
		System.out.println("Delete user by id");
		
	}
```
después:
```java
@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable int id) {
		
		System.out.println("Delete user by id");
		
		return ResponseEntity.ok(null);
	}
```

### Respuesta 404 NOT FOUND:
#### GET --> 404 NOT FOUND
forzamos un codigo de respuesta NOT FOUND y devolvemos la respuesta http con ResponseEntity.notFound().build()
```java
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
		
		System.out.println("Recovery user by id");
		
		UserDTO userDTO = null; //new UserDTO(1, "Jesús");
		//userDTO.setAge(33);
		//userDTO.setLastName("Romero Vidal");
		
		if(userDTO == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(userDTO);
	}
```

### Anidación de recursos
Cuando una entidad como Users tiene relaciones 1-n con otras entidades como por ejemplo accounts, existen varias formas de recuperar la lista de accounts a partir del user pero, solo una de ellas se considera una buena práctica.

Esta es la forma correcta de recuperar todas las cuentas de un usuario. Primero en la ruta se accede al usuario y después a las cuentas:

```java
	@GetMapping("/{id}/accounts")
	public ResponseEntity<List<AccountDTO>> getUserAccounts(@PathVariable int id) {
		
		List<AccountDTO> list = List.of(new AccountDTO("Google"), new AccountDTO( "Twitter"), new AccountDTO("Escuela IT"));
		
		return ResponseEntity.ok(list);
	}
```

se considera mala práctica recuperar este recurso escribiendo al url con query param así:
http://localhost.com/users/accounts?id=...

otro ejemplo es que necesitemos para un usuario específico solo una cuenta en específico:

```java
	@GetMapping("/{id}/accounts/{idAccount}")
	public ResponseEntity<AccountDTO> getUserAccountById(@PathVariable int id, @PathVariable int idAccount) {
		
		AccountDTO account = new AccountDTO("Google");
		
		return ResponseEntity.ok(account);
	}
```

En este ultimo ejemplo la forma correcta es indicar en la url el usuario concreto copn su id para luego acceder al recurso Account con su id también. en este punto podríamos preguntarnos porqué no acceder directamete con Account{id} si este es un id único entre todas las cuentas... La respuesta es que según el estandar REST se debería acceder recurso a recurso "/{id}/accounts/{idAccount}" así tenemos en cuenta aquellas API que no tienen un identificador unico para cada recurso sino una combinacion de id de User y Account, por ejemplo:

User 1 - Account 1
User 1 - Account 2
User 1 - Account 3
User 2 - Account 1
User 2 - Account 2
User 2 - Account 3

Un caso diferente es como utilizar un endpooint para crear un recurso anidado--no solo recuperar recursos anidados como antes.
en Este caso deberiamos tener un controlador de Account con el CRUD de Account. Entonces en el método create de este recurso se haría dos llamadas, una para recuperar el usuario y otra para crear una cuenta que pertenezca al usuario recuperado previamente.

Hay mucha confusión sobre hasta donde llegar con el anidamiento. Una buena práctica es recuperar--Read- un segundo recurso a partir del primero pero, cuando ya queremos hacer otras operaciones CRUD--diferentes a simples lecturas--  es mejor hacer un split y generar un API REST (controladorREST) especificamente de Account.

### Buenas prácticas.
#### REST es orientado a recursos
![alt text](./docs/image4.png)
#### Concatenación de recursos
![alt text](./docs/image5.png)
#### Otros
![alt text](./docs/image6.png)
![alt text](./docs/image7.png)
![alt text](./docs/image8.png)
![alt text](./docs/image9.png)
![alt text](./docs/image10.png)


### Externalización
#### Externalización: @Value
nos interesa guardar configuraciones que sean modificables en el futuro een un archivo de configuración .properties, para separarlo del código de la aplicación. Ya que en nuestro código no queremos hardcodear nada que sea susceptible de cambio en el futuro.

Para acceder a esta configuración lo haremos por medio de una clase java decorada con la anotación de Spring @configuration para convertir la clase en un Bean de configuración de Spring que nos da la capacidad de guardar los valores de los archivos de configuracióhn en los atributos de la clase.

primero creamos el paquete confiration y creamos una clase ApplicationConfig.

```java
package es.edu.escuela_it.Miroservices.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

}
```

incluimos lo siguiente en el archivo de application.properties

```
spring.application.name=Miroservices Java Classes
app.year=2020
app.edition=Community
app.countries=es,ar,it,ca,br
```

La anotacion @Configuration lo que hace es incorporar esta clase en el contenedor de dependecias de Spring, en el core.

Por otro lado la anotación @Value rescata el valor del archivo application.properties y lo asigna al atributo

```java
package es.edu.escuela_it.Miroservices.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class ApplicationConfig {

	@Value("${spring.application.name}")
	private String name;
	
	@Value("${app.year}")
	private int year;
	
	@Value("${app.edition}")
	private String edition;
	
	@Value("${app.countries}")
	private String[] countries;
	
}
```

Ahora probaremos a recuperar estos valores desde la clase controladora HolaMundo. Creamos un atributo que contendrá el objeto ApplicationConfig y lo anotamos con @Autowired. @Autowired solicita al Core de dependencias de Spring el objeto ApplicationConfig y Spring va a fabricar por nosotros este objeto. Lo hacemos así, porque al crear el objeto Spring en vez de notros con un new, Spring lo va  a construir incorparando ya los valores en los atributos anotados con @Value.

```java
package es.edu.escuela_it.Miroservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import es.edu.escuela_it.Miroservices.configuration.ApplicationConfig;

@RestController
public class HolaMundoRest {

	@Autowired
	private ApplicationConfig appConfig;
	
	@GetMapping("/holaMundo")
	public String saludo() {
		System.out.println(this.appConfig.toString());
		return "Hola Mundo servicio Rest Java";
	}
}
```

comprobamos como al hcer la llamada rest /holaMundo nos imprime por consola
las propiedades que ha rescatado del archivo de congiguración:
```
ApplicationConfig(name=Miroservices Java Classes, year=2020, edition=Community, countries=[es, ar, it, ca, br])
```

#### Externalización: @ConfigurationProperties Prefix
Anotación para normalizar prefijos. Si el atributo tiene el mismo nombre que el sufijo de la propiedad deberemos de quitar las anotaciones @Value

```java
package es.edu.escuela_it.Miroservices.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "app")
public class ApplicationConfig {

	@Value("${spring.application.name}")
	private String name;
	
	private int year;
	
	private String edition;
	
	private String[] countries;
	
}
```

### Documentación con Swagger
#### Swagger: Setup
Necesitmaos incorporar springdoc-openapi-starter-webmvc-ui como dependencia en el pom.xml
```xml
	<dependency>
		<groupId>org.springdoc</groupId>
		<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
		<version>2.0.3</version>
	</dependency>
```

Creamnos una clase de configuración llamada OpenAPIConfig
que será un Bean que configura Swagger en el arranque del proyecto
y añadimos algunos datos de carátula
+info: https://www.bezkoder.com/spring-boot-swagger-3/

```java
package es.edu.escuela_it.Miroservices.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

	@Value("${openapi.dev-url}")
	  private String devUrl;

	  @Value("${openapi.prod-url}")
	  private String prodUrl;

	  @Bean
	  public OpenAPI myOpenAPI() {
	    Server devServer = new Server();
	    devServer.setUrl(devUrl);
	    devServer.setDescription("Server URL in Development environment");

	    Server prodServer = new Server();
	    prodServer.setUrl(prodUrl);
	    prodServer.setDescription("Server URL in Production environment");

	    Contact contact = new Contact();
	    contact.setEmail("jesus.romerovidal@gmail.com");
	    contact.setName("Jesús");
	    contact.setUrl("https://github.com/js-rom");

	    License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

	    Info info = new Info()
	        .title("Users Management API")
	        .version("2.0")
	        .contact(contact)
	        .description("This API exposes endpoints to manage users accounts.").termsOfService("https://www._.com/terms")
	        .license(mitLicense);

	    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
	    
	  }
}

```

link de acceso a la UI de swagger de nuestro proyecto: 
http://localhost:8080/escuelait/api/v2/microservices/swagger-ui/index.html#/

### Swagger. Models Annotation

utilizamos la anotación @Schema

```java
package es.edu.escuela_it.Miroservices.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Details about the user")
public class UserDTO extends RepresentationModel<UserDTO> {
	
	
	@Schema(description = "The unique ID of the user", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	@lombok.NonNull
	private int id;
	
	@Schema(description = "Name of the user", example = "Jesús")
	@lombok.NonNull
	private String name;
	
	@Schema(description = "Last name of the user", example = "Romero")
	private String lastName;
	
	@Schema(description = "Age of the user", example = "33")
	@ToString.Exclude
	private int age;

}
```
![alt text](./docs/image15.png) 

### Swagger. Endpoints Annotations
anotaciones utilizadas:
- @Tag
- @Operation
- @ ApiResponses
- @ApiResponse

```java
//Swagger:
@Tag(name = "Users", description = "the Users Api")
public class UsersControllerRest {

	public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {

		System.out.println("Recovery user by id");

		UserDTO userDTO = new UserDTO(1, "Jesús");
		userDTO.setAge(33);
		userDTO.setLastName("Romero Vidal");

		Link withSelfRel = linkTo(methodOn(UsersControllerRest.class).getUserById(userDTO.getId())).withSelfRel();
		userDTO.add(withSelfRel);

		return ResponseEntity.ok(userDTO);
	}

	@Operation(summary = "Fetch all users", description = "fetches all users entities and their data from data source")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	@GetMapping
	public ResponseEntity<CollectionModel<UserDTO>> listAllUsers() {

		List<UserDTO> list = List.of(new UserDTO(1, "Jesús"), new UserDTO(2, "Miguel"), new UserDTO(3, "Álvaro"));

		for (UserDTO userDTO : list) {

			Link withSelfRel = linkTo(methodOn(UsersControllerRest.class).getUserById(userDTO.getId())).withSelfRel();
			userDTO.add(withSelfRel);

			Link accountsRel = linkTo(methodOn(UsersControllerRest.class).getUserAccounts(userDTO.getId()))
					.withRel("accounts");
			userDTO.add(accountsRel);
		}
```
![alt text](./docs/image16.png) 

### Swagger. Separar implementacion de documentación.

Podemos poner toda las anotaciones de swagger es una interfaz
e implementar esta interfaz desde el endpoin o el modelo.


### Hateoas. Navegación de la API.
#### Hateoas. Setup
Primero incorporamos la dependencia al pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```
#### Hateoas. Navegación self resource
Los modelos deben extender de RepresentationModel\<Model>
```java
public class UserDTO extends RepresentationModel<UserDTO> {
    //… 
}
```
Por otro lado, en la clase controller hacemos los siguientes import:

```java
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
```

en cada método utilizariamos el siguiente código para conseguir la nevegacioón consigo mismo, es decir un endpoint GET además de devolver un recurso debe proveer información (link) de donde se encuentra este mismo recurso.

```java
Link withSelfRel =
linkTo(methodOn(UsersControllerRest.class).getUserById(userDTO.getId())).withSelfRel();
userDTO.add(withSelfRel);
```

ejemplo:
```java
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {

		System.out.println("Recovery user by id");

		UserDTO userDTO = new UserDTO(1, "Jesús");
		userDTO.setAge(33);
		userDTO.setLastName("Romero Vidal");

		Link withSelfRel = linkTo(methodOn(UsersControllerRest.class).getUserById(userDTO.getId())).withSelfRel();
		userDTO.add(withSelfRel);

		return ResponseEntity.ok(userDTO);
	}
```
vemos como nos devuelve el link en la respuesta:
![alt text](./docs/image11.png)

#### Hateoas. Navegación self list
En el método listAllUsers() antes devolvíamos una List de usuarios. ahora devolvemos un objebto CollectionModel de Hateoas que envuelve a la lista de usuarios

```java
	@GetMapping
	public ResponseEntity<CollectionModel<UserDTO>> listAllUsers() {
		
		List<UserDTO> list = List.of(new UserDTO(1, "Jesús"), new UserDTO(2, "Miguel"), new UserDTO(3, "Álvaro"));
		
		Link link = linkTo(methodOn(UsersControllerRest.class).listAllUsers()).withSelfRel();
		CollectionModel<UserDTO> result = CollectionModel.of(list, link);
		return ResponseEntity.ok(result);
		
		//return ResponseEntity.ok(list);
	}
```

en la respues ahora podemos como por una parte devuelve la lista de users y por otra parte el link donde encontrar el recurso users que devuelve la lista:
![alt text](./docs/image12.png)

Lo mas apropiado sería que cada uno de los objetos users que devuelve la lista tuviese un link donde encontrar ese recurso concreto. Lo conseguimos con el siguiente código:
```java
	@GetMapping
	public ResponseEntity<CollectionModel<UserDTO>> listAllUsers() {
		
		List<UserDTO> list = List.of(new UserDTO(1, "Jesús"), new UserDTO(2, "Miguel"), new UserDTO(3, "Álvaro"));
		
		for	(UserDTO userDTO : list) {
			Link withSelfRel = linkTo(methodOn(UsersControllerRest.class).getUserById(userDTO.getId())).withSelfRel();
			userDTO.add(withSelfRel);
		}
		
		Link link = linkTo(methodOn(UsersControllerRest.class).listAllUsers()).withSelfRel();
		CollectionModel<UserDTO> result = CollectionModel.of(list, link);
		return ResponseEntity.ok(result);
		
		//return ResponseEntity.ok(list);
	}
```

Observamos como hemos utilizado el códico de Navegación self resource para añadir el link a cada recurso concreto UsersDTO

![alt text](./docs/image13.png)

#### Hateoas. Navegación inter-resource

Estaría bien devolver junto con el link a cada usuario otros link a las todas las entidades relacionadas con el usuario como Accounts. ahora no indicamo withSelfRel() sino la relación a la otra entidad con withRel("accounts")

```java
@GetMapping
	public ResponseEntity<CollectionModel<UserDTO>> listAllUsers() {

		List<UserDTO> list = List.of(new UserDTO(1, "Jesús"), new UserDTO(2, "Miguel"), new UserDTO(3, "Álvaro"));

		for (UserDTO userDTO : list) {

			Link withSelfRel = linkTo(methodOn(UsersControllerRest.class).getUserById(userDTO.getId())).withSelfRel();
			userDTO.add(withSelfRel);

			Link accountsRel = linkTo(methodOn(UsersControllerRest.class).getUserAccounts(userDTO.getId()))
					.withRel("accounts");
			userDTO.add(accountsRel);
		}

		Link link = linkTo(methodOn(UsersControllerRest.class).listAllUsers()).withSelfRel();
		CollectionModel<UserDTO> result = CollectionModel.of(list, link);
		return ResponseEntity.ok(result);

	}
```

la respuesta quedaría así:

![alt text](./docs/image14.png)

