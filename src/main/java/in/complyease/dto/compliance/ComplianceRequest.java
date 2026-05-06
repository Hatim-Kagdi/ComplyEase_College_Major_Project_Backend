package in.complyease.dto.compliance;

import java.time.LocalDate;

import in.complyease.enums.ComplianceType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class ComplianceRequest {
    @NotNull(message = "Business ID is required")
    private Integer businessId;

    @NotNull(message = "Compliance type is required")
    private ComplianceType complianceType;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}
