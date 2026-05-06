package in.complyease.dto.Business;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessResponse {
	private int businessId;
	private String businessName;
	private String businessGstNumber;
}
