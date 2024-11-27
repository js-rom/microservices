package es.edu.escuela_it.Miroservices.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;;

public class CIFValidator implements ConstraintValidator<CIF, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		return value.length() == 9;
	}
}
