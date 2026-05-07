package in.complyease.dto.business;

import in.complyease.enums.CAAssignmentStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessResponse {
	private int businessId;
	private String businessName;
	private String businessGstNumber;
	private CAAssignmentStatus caAssignmentStatus;
}
