package in.complyease.dto.notification;

import java.time.LocalDateTime;

import in.complyease.enums.NotificationStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private int notificationId;

    private int businessId;

    private String businessName;

    private String message;

    private LocalDateTime sentAt;

    private NotificationStatus status;
}