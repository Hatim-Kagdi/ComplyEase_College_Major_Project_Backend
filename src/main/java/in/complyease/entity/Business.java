package in.complyease.entity;

import java.util.List;

import org.hibernate.annotations.*;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;

@Entity
@Table(name = "businesses")
@SQLDelete(sql = "UPDATE businesses SET is_deleted = true WHERE business_id = ?")
@SQLRestriction("is_deleted = false")
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
	
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Compliance> compliances;
	
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Document> documents;
	
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Notification> notifications;

	public int getId() {
		return businessId;
	}

	public void setId(int id) {
		this.businessId = id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessGstNumber() {
		return businessGstNumber;
	}

	public void setBusinessGstNumber(String businessGstNumber) {
		this.businessGstNumber = businessGstNumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Business(int id, String businessName, String businessGstNumber, User user) {
		super();
		this.businessId = id;
		this.businessName = businessName;
		this.businessGstNumber = businessGstNumber;
		this.user = user;
	}

	public Business() {
	}	
}
