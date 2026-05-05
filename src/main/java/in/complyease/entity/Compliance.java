package in.complyease.entity;

import java.time.LocalDate;

import org.hibernate.annotations.*;

import in.complyease.enums.ComplianceStatus;
import in.complyease.enums.ComplianceType;
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "compliances")
@SQLDelete(sql = "UPDATE compliances SET is_deleted = true WHERE compliance_id = ?")
@SQLRestriction("is_deleted = false")
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

	public int getComplianceId() {
		return complianceId;
	}

	public void setComplianceId(int complianceId) {
		this.complianceId = complianceId;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public ComplianceType getComplianceType() {
		return complianceType;
	}

	public void setComplianceType(ComplianceType complianceType) {
		this.complianceType = complianceType;
	}

	public LocalDate getComplianceDueDate() {
		return complianceDueDate;
	}

	public void setComplianceDueDate(LocalDate complianceDueDate) {
		this.complianceDueDate = complianceDueDate;
	}

	public ComplianceStatus getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(ComplianceStatus complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public Compliance(int complianceId, Business business, ComplianceType complianceType, LocalDate complianceDueDate,
			ComplianceStatus complianceStatus) {
		super();
		this.complianceId = complianceId;
		this.business = business;
		this.complianceType = complianceType;
		this.complianceDueDate = complianceDueDate;
		this.complianceStatus = complianceStatus;
	}

	public Compliance() {
	}
	
	
}
