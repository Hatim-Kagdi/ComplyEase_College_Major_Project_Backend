package in.complyease.entity;

import java.time.LocalDate;

import org.hibernate.annotations.*;

import in.complyease.enums.ComplianceStatus;
import in.complyease.enums.ComplianceType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "compliances")
@SQLDelete(sql = "UPDATE compliances SET is_deleted = true WHERE compliance_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Compliance extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "compliance_id")
	private int complianceId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id")
	private Business business;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "compliance_type")
	private ComplianceType complianceType;
	
	@Column(name = "due_date")
	private LocalDate complianceDueDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ComplianceStatus complianceStatus;
}
