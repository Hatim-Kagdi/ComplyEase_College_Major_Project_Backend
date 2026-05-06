package in.complyease.entity;

import org.hibernate.annotations.*;

import in.complyease.enums.DocumentType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "documents")
@SQLDelete(sql = "UPDATE documents SET is_deleted = true WHERE document_id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Document extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_id")
	private int documentId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_id")
	private Business business;
	
	@Column(name = "file_name")
	private String documentFileName;
	
	@Column(name = "file_url")
	private String documentFileUrl;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "document_type")
	private DocumentType documentType;
}
