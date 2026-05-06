package in.complyease.dto.compliance;

import java.time.LocalDate;

import in.complyease.enums.ComplianceStatus;
import in.complyease.enums.ComplianceType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceResponse {
    private int complianceId;
    private int businessId;
    private String businessName;

    private ComplianceType complianceType;
    private LocalDate dueDate;
    private ComplianceStatus status;
    
    
}
