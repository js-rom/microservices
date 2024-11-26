package es.edu.escuela_it.Miroservices.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.edu.escuela_it.Miroservices.model.AccountDTO;
import es.edu.escuela_it.Miroservices.model.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
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

		Link link = linkTo(methodOn(UsersControllerRest.class).listAllUsers()).withSelfRel();
		CollectionModel<UserDTO> result = CollectionModel.of(list, link);
		return ResponseEntity.ok(result);

	}

	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {

		System.out.println("Creating user" + userDTO.getId());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {

		System.out.println("Updating data");

		return ResponseEntity.ok(userDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable int id) {

		System.out.println("Delete user by id");

		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}/accounts")
	public ResponseEntity<List<AccountDTO>> getUserAccounts(@PathVariable int id) {

		List<AccountDTO> list = List.of(new AccountDTO("Google"), new AccountDTO("Twitter"),
				new AccountDTO("Escuela IT"));

		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}/accounts/{idAccount}")
	public ResponseEntity<AccountDTO> getUserAccountById(@PathVariable int id, @PathVariable int idAccount) {

		AccountDTO account = new AccountDTO("Google");

		return ResponseEntity.ok(account);
	}

}
