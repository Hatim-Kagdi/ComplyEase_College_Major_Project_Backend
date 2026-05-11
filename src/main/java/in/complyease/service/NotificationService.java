package in.complyease.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.dto.notification.NotificationResponse;
import in.complyease.entity.*;
import in.complyease.enums.NotificationStatus;
import in.complyease.repository.*;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notificationRepository;

    @Autowired private UserRepository userRepository;

    //GET USER NOTIFICATIONS
    public List<NotificationResponse> getUserNotifications(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                        new RuntimeException("User not found"));
        List<Business> businesses = user.getBusinesses();

        List<Notification> notifications = notificationRepository
        		.findByBusinessInOrderByNotificationSentAtDesc(businesses);

        return notifications.stream()
                .map(notification ->
                        new NotificationResponse(
                                notification.getNotificationId(),
                                notification.getBusiness().getBusinessId(),
                                notification.getBusiness().getBusinessName(),
                                notification.getNotificationMessage(),
                                notification.getNotificationSentAt(),
                                notification.getNotificationStatus()
                        )).toList();
    }

    //CREATE NOTIFICATION
    @Transactional
    public Notification createNotification(Business business,String message) {
        Notification notification = new Notification();
        notification.setBusiness(business);
        notification.setNotificationMessage(message);
        notification.setNotificationSentAt(LocalDateTime.now());
        notification.setNotificationStatus(NotificationStatus.SENT);
        return notificationRepository.save(notification);
    }
}