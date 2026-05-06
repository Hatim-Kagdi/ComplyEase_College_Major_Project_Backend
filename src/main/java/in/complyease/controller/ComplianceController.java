package in.complyease.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.complyease.dto.compliance.ComplianceRequest;
import in.complyease.dto.compliance.ComplianceResponse;
import in.complyease.service.ComplianceService;

@RestController
@RequestMapping("/user/compliance")
public class ComplianceController {

    @Autowired
    private ComplianceService complianceService;

    //CREATE
    @PostMapping
    public ResponseEntity<ComplianceResponse> createCompliance(
            @RequestBody ComplianceRequest request,
            Principal principal) {

        String email = principal.getName();
        return ResponseEntity.ok(
                complianceService.createCompliance(request, email)
        );
    }

    //GET ALL
    @GetMapping
    public ResponseEntity<List<ComplianceResponse>> getAllCompliances(
            Principal principal) {

        String email = principal.getName();
        return ResponseEntity.ok(
                complianceService.getUserCompliances(email)
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ComplianceResponse> getComplianceById(
            @PathVariable int id,
            Principal principal) {

        String email = principal.getName();
        return ResponseEntity.ok(
                complianceService.getComplianceById(id, email)
        );
    }

    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ComplianceResponse> updateCompliance(
            @PathVariable int id,
            @RequestBody ComplianceRequest request,
            Principal principal) {

        String email = principal.getName();
        return ResponseEntity.ok(
                complianceService.updateCompliance(id, request, email)
        );
    }

    // UPDATE STATUS
    @PatchMapping("/{id}/status")
    public ResponseEntity<ComplianceResponse> updateStatus(
            @PathVariable int id,
            @RequestParam String status,
            Principal principal) {

        String email = principal.getName();
        return ResponseEntity.ok(
                complianceService.updateComplianceStatus(id, email, status)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompliance(
            @PathVariable int id,
            Principal principal) {

        String email = principal.getName();
        complianceService.deleteCompliance(id, email);

        return ResponseEntity.ok("Compliance deleted successfully");
    }
}