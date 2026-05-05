package in.complyease.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.*;

import in.complyease.enums.NotificationStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET is_deleted = true WHERE notification_id = ?")
@SQLRestriction("is_deleted = false")
public class Notification extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private int notificationId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id")
	private Business business;
	
	@Column(name = "message")
	private String notificationMessage;
	
	@Column(name = "sent_at")
	private LocalDateTime notificationSentAt;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private NotificationStatus notificationStatus;

	public int getNotification_id() {
		return notificationId;
	}

	public void setNotification_id(int notification_id) {
		this.notificationId = notification_id;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public LocalDateTime getNotificationSentAt() {
		return notificationSentAt;
	}

	public void setNotificationSentAt(LocalDateTime notificationSentAt) {
		this.notificationSentAt = notificationSentAt;
	}

	public NotificationStatus getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(NotificationStatus notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	public Notification(int notification_id, Business business, String notificationMessage,
			LocalDateTime notificationSentAt, NotificationStatus notificationStatus) {
		super();
		this.notificationId = notification_id;
		this.business = business;
		this.notificationMessage = notificationMessage;
		this.notificationSentAt = notificationSentAt;
		this.notificationStatus = notificationStatus;
	}

	public Notification() {
	}
}
