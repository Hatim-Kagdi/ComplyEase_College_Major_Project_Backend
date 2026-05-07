package in.complyease.entity;

import java.util.List;

import org.hibernate.annotations.*;

import in.complyease.enums.CAAssignmentStatus;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "businesses")
@SQLDelete(sql = "UPDATE businesses SET is_deleted = true WHERE business_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Business extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "business_id")
	private int businessId;
	
	@Column(name = "business_name")
	private String businessName;
	
	@Column(name = "business_gst_number")
	private String businessGstNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_ca_id")
	private User assignedCA;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ca_assignment_status")
	private CAAssignmentStatus caAssignmentStatus;
	
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Compliance> compliances;
	
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Document> documents;
	
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Notification> notifications;
	
}
