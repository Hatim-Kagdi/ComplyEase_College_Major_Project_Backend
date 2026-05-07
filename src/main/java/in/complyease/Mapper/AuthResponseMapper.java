package in.complyease.Mapper;

import in.complyease.dto.AuthResponseDTO;
import in.complyease.enums.UserRole;

public class AuthResponseMapper {
	
	public static AuthResponseDTO mapToAuthDTO(String token, UserRole role, String name, String email) {
		 return new AuthResponseDTO(
	                token,
	                role,
	                name,
	                email
	        );
	}

}
