package es.edu.escuela_it.Miroservices.model;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import es.edu.escuela_it.Miroservices.validators.CIF;
import es.edu.escuela_it.Miroservices.validators.GroupValidatorOnCreate;
import es.edu.escuela_it.Miroservices.validators.GroupValidatorOnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Details about the user")
public class UserDTO extends RepresentationModel<UserDTO> {
	
	
	@Schema(description = "The unique ID of the user", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	@lombok.NonNull
	@NotNull // jakarta.validation
	private Integer id;
	
	@Schema(description = "Name of the user", example = "Jesús")
	@lombok.NonNull
	@NotBlank(message = "Error, el nombre debe tener al menos un caracter") // jakarta.validation
	private String name;
	
	@Schema(description = "Last name of the user", example = "Romero")
	@Size(min = 6, max = 20) // jakarta.validation
	private String lastName;
	
	@Schema(description = "Age of the user", example = "33")
	@ToString.Exclude
	@NotNull // jakarta.validation
	@Positive // jakarta.validation
	@Min(18) // jakarta.validation
	@Max(90) // jakarta.validation
	private Integer age;
	
	@Email
	private String email;
	
	@AssertTrue(message="{app.field.active.error}", groups = GroupValidatorOnCreate.class) // jakarta.validation
	@AssertFalse(groups = GroupValidatorOnUpdate.class) // jakarta.validation
	private boolean active;
	
	@Past(message="{app.field.birth_day.error}") // jakarta.validation
	// @FutureOrPresent // jakarta.validation
	private LocalDate birthday;
	
	@CIF
	private String cif;
	
}
