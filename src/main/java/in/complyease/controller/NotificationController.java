package in.complyease.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.complyease.service.NotificationService;

@RestController
@RequestMapping("/user/notification")
public class NotificationController {

    @Autowired private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getUserNotifications(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(notificationService.getUserNotifications(email));
    }
}