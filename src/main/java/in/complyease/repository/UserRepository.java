package in.complyease.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.complyease.entity.User;
import in.complyease.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	Optional<User> findByEmail(String email);
	
	List<User> findByRole(UserRole role);
	
	List<User> findByRoleAndIsApproved(UserRole role, boolean isApproved);
}
