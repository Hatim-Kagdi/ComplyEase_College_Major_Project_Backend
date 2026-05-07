package in.complyease.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.complyease.service.*;

@RestController
@RequestMapping("/ca")
public class CAController {
	
	@Autowired private BusinessService businessService;
	@Autowired private ComplianceService complianceService;
	@Autowired private DocumentService documentService;
	@Autowired private CADashboardStatsService dashboardService;
	
	@GetMapping("/business")
    public ResponseEntity<?> getAssignedBusinesses(
            Principal principal
    ) {

        String email = principal.getName();

        return ResponseEntity.ok(
                businessService.getAssignedBusinesses(email)
        );
    }
	
	@GetMapping("/compliance")
	public ResponseEntity<?> getAssignedBusinessCompliances(
	        Principal principal
	) {

	    String email = principal.getName();

	    return ResponseEntity.ok(
	            complianceService.getAssignedBusinessCompliances(email)
	    );
	}
	
	@GetMapping("/document")
	public ResponseEntity<?> getAssignedBusinessDocuments(
	        Principal principal
	) {

	    String email = principal.getName();

	    return ResponseEntity.ok(
	            documentService.getAssignedBusinessDocuments(email)
	    );
	}
	
	@PatchMapping("/compliance/{id}/status")
    public ResponseEntity<?> updateComplianceStatus(
            @PathVariable int id,
            @RequestParam String status,
            Principal principal
    ) {

        String email = principal.getName();

        return ResponseEntity.ok(
                complianceService.updateComplianceStatusByCA(
                        id,
                        email,
                        status
                )
        );
    }
	
	@GetMapping("/dashboard/stats")
	public ResponseEntity<?> getDashboardStats(
	        Principal principal
	) {

	    String email = principal.getName();

	    return ResponseEntity.ok(
	            dashboardService.getCADashboardStats(email)
	    );
	}

}
