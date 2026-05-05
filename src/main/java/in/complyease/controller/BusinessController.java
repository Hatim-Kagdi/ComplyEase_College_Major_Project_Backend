package in.complyease.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.complyease.dto.Business.BusinessRequest;
import in.complyease.dto.Business.BusinessResponse;
import in.complyease.service.BusinessService;

@RestController
@RequestMapping("/user")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping("/business")
    public ResponseEntity<?> createBusiness(@RequestBody BusinessRequest request,
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
}