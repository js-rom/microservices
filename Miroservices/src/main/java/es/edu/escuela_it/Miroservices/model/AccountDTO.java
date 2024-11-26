package es.edu.escuela_it.Miroservices.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountDTO {

	private int id;
	@NonNull
	private String name;
	
}
