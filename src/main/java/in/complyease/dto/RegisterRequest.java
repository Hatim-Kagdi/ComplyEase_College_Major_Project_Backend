package in.complyease.dto;

import in.complyease.entity.Business;
import in.complyease.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	private String name;
	private String email;
	private String password;
	private String role;
}
