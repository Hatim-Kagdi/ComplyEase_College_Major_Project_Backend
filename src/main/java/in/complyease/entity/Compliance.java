package in.complyease.entity;

import java.time.LocalDate;
import java.util.*;

import org.hibernate.annotations.*;

import in.complyease.enums.ComplianceStatus;
import in.complyease.enums.ComplianceType;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
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
	
	// Link to Documents
    @OneToMany(mappedBy = "compliance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Document> documents = new ArrayList<>();

    // Communication field for the CA
    @Column(name = "ca_remarks", length = 500)
    private String caRemarks;
}
