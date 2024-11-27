package es.edu.escuela_it.Miroservices.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Documented
@Constraint(validatedBy = CIFValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CIF {
	String message() default "Invalid CIF Number";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
