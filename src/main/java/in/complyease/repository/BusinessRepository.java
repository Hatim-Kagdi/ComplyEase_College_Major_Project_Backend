package in.complyease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.complyease.entity.Business;
import in.complyease.entity.User;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Integer>{
	List<Business> findByUser(User user);
}
