package in.complyease.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.complyease.dto.Business.BusinessRequest;
import in.complyease.dto.Business.BusinessResponse;
import in.complyease.service.BusinessService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping("/business")
    public ResponseEntity<?> createBusiness(@Valid @RequestBody BusinessRequest request,
                                            Principal principal) {

        String email = principal.getName();

        BusinessResponse response = businessService.createBusiness(request, email);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/business")
    public ResponseEntity<List<BusinessResponse>> getBusinesses(Principal principal) {

        String email = principal.getName();

        return ResponseEntity.ok(
                businessService.getUserBusinesses(email)
        );
    }
    
    @GetMapping("/business/{id}")
    public ResponseEntity<?> getBusinessById(@PathVariable int id) {
        return ResponseEntity.ok(businessService.getBusinessById(id));
    }

    @DeleteMapping("/business/{id}")
    public ResponseEntity<?> deleteBusiness(@PathVariable int id) {
        businessService.deleteBusiness(id);
        return ResponseEntity.ok("Deleted successfully");
    }
    
    @PutMapping("/business/{id}")
    public ResponseEntity<?> updateBusiness(@PathVariable int id,
                                            @RequestBody BusinessRequest request,
                                            Principal principal) {
    	String email = principal.getName();
        return ResponseEntity.ok(
            businessService.updateBusiness(id, request, email)
        );
    }
}