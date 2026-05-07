package in.complyease.dto.business;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessRequest {
	    @NotBlank(message = "Business name is required")
	    private String businessName;

	    @NotBlank(message = "GST number is required")
	    private String businessGstNumber;
	}
