package in.complyease.entity;

import java.util.List;

import org.hibernate.annotations.*;

import in.complyease.enums.UserRole;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity{
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column
	    private Long id;
	    
	    @Column
	    private String name;

	    @Column(unique = true)
	    private String email;
	    
	    @Column
	    private String password;
	    
	    @Enumerated(EnumType.STRING)
	    @Column
	    private UserRole role;
	    
	    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private List<Business> businesses;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public UserRole getRole() {
			return role;
		}

		public void setRole(UserRole role) {
			this.role = role;
		}
}
