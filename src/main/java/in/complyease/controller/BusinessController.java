package in.complyease.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.complyease.dto.UserDashboardStatsDTO;
import in.complyease.dto.business.AssignedCADTO;
import in.complyease.dto.business.BusinessRequest;
import in.complyease.dto.business.BusinessResponse;
import in.complyease.service.BusinessService;
import in.complyease.service.UserDashboardService;
import jakarta.validation.Valid;

@RestController
public class BusinessController {

    @Autowired private BusinessService businessService;
    
    @Autowired private UserDashboardService dashboardService;

    @PostMapping("/user/business")
    public ResponseEntity<?> createBusiness(@Valid @RequestBody BusinessRequest request,
                                            Principal principal) {
        String email = principal.getName();
        BusinessResponse response = businessService.createBusiness(request, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/business")
    public ResponseEntity<List<BusinessResponse>> getBusinesses(Principal principal) {

        String email = principal.getName();

        return ResponseEntity.ok(
                businessService.getUserBusinesses(email)
        );
    }
    
    @GetMapping("/user/business/{id}")
    public ResponseEntity<?> getBusinessById(@PathVariable int id) {
        return ResponseEntity.ok(businessService.getBusinessById(id));
    }

    @DeleteMapping("/user/business/{id}")
    public ResponseEntity<?> deleteBusiness(@PathVariable int id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.ok("Deleted successfully");
    }
    
    @PutMapping("/user/business/{id}")
    public ResponseEntity<?> updateBusiness(@PathVariable int id,
                                            @RequestBody BusinessRequest request,
                                            Principal principal) {
    	String email = principal.getName();
        return ResponseEntity.ok(
            businessService.updateBusiness(id, request, email)
        );
    }
  
    @PatchMapping("/user/business/{id}/request-ca")
    public ResponseEntity<?> requestCA(
            @PathVariable int id,
            Principal principal) {

        String email = principal.getName();

        return ResponseEntity.ok(
                businessService.requestCA(id, email)
        );
    }
    
    @GetMapping("/user/dashboard/stats")
    public ResponseEntity<UserDashboardStatsDTO> getDashboardStats(
            Principal principal
    ) {

        String email = principal.getName();

        return ResponseEntity.ok(
                dashboardService.getUserDashboardStats(email)
        );
    }
   
}