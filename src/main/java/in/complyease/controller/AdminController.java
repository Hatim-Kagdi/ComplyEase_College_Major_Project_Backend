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

    @Autowired private BusinessService businessService;  
    @Autowired private UserService userService;   
    @Autowired private CaService caService;  
    @Autowired private AdminDashboardService adminDashboardService;

    @GetMapping("/business/requested")
    public ResponseEntity<List<BusinessResponse>> getRequestedBusinesses() {
        return ResponseEntity.ok(businessService.getRequestedBusinesses());
    }
    
    @GetMapping("/ca")
    public ResponseEntity<List<CADTO>> getAllCAUsers() {
        return ResponseEntity.ok(caService.getAllCAUsers());
    }
    
    @GetMapping("/ca/pending")
    public ResponseEntity<?> getPendingCAs() {
        return ResponseEntity.ok(caService.getPendingCAs());
    }
    
    @PatchMapping("/ca/{id}/approve")
    public ResponseEntity<?> approveCA(@PathVariable Long id) {
        return ResponseEntity.ok(caService.approveCA(id));
    }
    
    @PatchMapping("/ca/{id}/reject")
    public ResponseEntity<?> rejectCA(@PathVariable Long id){
    	System.out.println("CA reject controller");
    	return ResponseEntity.ok(caService.rejectCA(id));
    }
    
    @PatchMapping("/business/{businessId}/assign-ca/{caId}")
    public ResponseEntity<?> assignCA(@PathVariable int businessId,@PathVariable Long caId) {
        return ResponseEntity.ok(businessService.assignCA(businessId, caId));
    }
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @PatchMapping("/users/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleUserStatus(id));
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }
    
    @GetMapping("/business")
    public ResponseEntity<?> getAllBusinesses() {
        return ResponseEntity.ok(businessService.getAllBusinesses());
    }
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getPlatformStats() {
        return ResponseEntity.ok(adminDashboardService.getPlatformStats());
    }
}
