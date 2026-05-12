package in.complyease.dto;

import in.complyease.enums.CAApprovalStatus;
import in.complyease.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
	
	private String token;
	private UserRole role;
	private String name;
	private String email;
	private CAApprovalStatus approvalStatus;

}
