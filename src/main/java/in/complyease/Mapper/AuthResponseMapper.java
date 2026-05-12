package in.complyease.Mapper;

import in.complyease.dto.AuthResponseDTO;
import in.complyease.enums.CAApprovalStatus;
import in.complyease.enums.UserRole;

public class AuthResponseMapper {
	
	public static AuthResponseDTO mapToAuthDTO(String token, UserRole role, String name, String email, CAApprovalStatus approvalStatus) {
		 return new AuthResponseDTO(
	                token,
	                role,
	                name,
	                email,
	                approvalStatus
	        );
	}

}
