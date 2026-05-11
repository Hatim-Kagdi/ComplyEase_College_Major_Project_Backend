package in.complyease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.complyease.entity.Notification;
import in.complyease.entity.Business;
import in.complyease.enums.NotificationStatus;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {

    List<Notification> findByBusinessInOrderByNotificationSentAtDesc(
            List<Business> businesses
    );

    long countByBusinessInAndNotificationStatus(
            List<Business> businesses,
            NotificationStatus status
    );
}