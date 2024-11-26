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
