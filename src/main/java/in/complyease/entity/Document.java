package in.complyease.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.*;

import in.complyease.enums.DocumentType;
import jakarta.persistence.*;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
@SQLDelete(sql = "UPDATE documents SET is_deleted = true WHERE document_id = ?")
@SQLRestriction("is_deleted = false")
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

	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}

	public String getDocumentFileUrl() {
		return documentFileUrl;
	}

	public void setDocumentFileUrl(String documentFileUrl) {
		this.documentFileUrl = documentFileUrl;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public Document(int documentId, Business business, String documentFileName, String documentFileUrl,
			DocumentType documentType) {
		super();
		this.documentId = documentId;
		this.business = business;
		this.documentFileName = documentFileName;
		this.documentFileUrl = documentFileUrl;
		this.documentType = documentType;
	}

	public Document() {
	}
	
	
}
