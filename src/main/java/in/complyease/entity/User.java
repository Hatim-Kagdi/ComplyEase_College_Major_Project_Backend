package in.complyease.entity;

import java.util.List;

import org.hibernate.annotations.*;

import in.complyease.enums.DocumentType;
import in.complyease.enums.UserRole;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
	    
	    @OneToMany(mappedBy = "assignedCA", fetch = FetchType.LAZY)
	    private List<Business> assignedBusinesses;

}
