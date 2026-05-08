package in.complyease.dto.admin;

import in.complyease.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserManagementDTO {

    private Long id;

    private String name;

    private String email;

    private UserRole role;

    private boolean isActive;
    
    private boolean isApproved;
}