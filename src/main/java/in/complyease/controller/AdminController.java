package in.complyease.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.complyease.dto.business.*;
import in.complyease.service.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/business/requested")
    public ResponseEntity<List<BusinessResponse>> getRequestedBusinesses() {

        return ResponseEntity.ok(
                businessService.getRequestedBusinesses()
        );
    }
    
    @GetMapping("/ca")
    public ResponseEntity<List<CADTO>> getAllCAUsers() {

        return ResponseEntity.ok(
                userService.getAllCAUsers()
        );
    }
    
    @PatchMapping("/business/{businessId}/assign-ca/{caId}")
    public ResponseEntity<?> assignCA(
            @PathVariable int businessId,
            @PathVariable Long caId) {

        return ResponseEntity.ok(
                businessService.assignCA(businessId, caId)
        );
    }
}
