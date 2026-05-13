package in.complyease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.complyease.entity.Document;
import in.complyease.entity.Business;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByBusiness(Business business);

    List<Document> findByBusinessIn(List<Business> businesses);
    
    List<Document> findByCompliance_ComplianceId(int complianceId);
}