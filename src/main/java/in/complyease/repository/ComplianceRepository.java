package in.complyease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.complyease.entity.Compliance;
import in.complyease.entity.Business;

@Repository
public interface ComplianceRepository extends JpaRepository<Compliance , Integer>{
	// Get all compliances for a specific business
    List<Compliance> findByBusiness(Business business);

    // Optional (useful later): get all compliances for multiple businesses
    List<Compliance> findByBusinessIn(List<Business> businesses);

}
