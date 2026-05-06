package in.complyease.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.*;

import in.complyease.enums.DocumentType;
import in.complyease.enums.NotificationStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET is_deleted = true WHERE notification_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
