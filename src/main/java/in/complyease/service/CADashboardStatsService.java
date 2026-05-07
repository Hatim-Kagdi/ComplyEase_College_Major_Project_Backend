package in.complyease.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.complyease.dto.dashboard.CADashboardStatsDTO;
import in.complyease.entity.User;
import in.complyease.enums.ComplianceStatus;
import in.complyease.repository.*;

@Service
public class CADashboardStatsService {
	
	@Autowired private ComplianceRepository complianceRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private BusinessRepository businessRepository;
	
	public CADashboardStatsDTO getCADashboardStats(
	        String email
	) {

	    User ca = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException("CA not found"));

	    long totalAssignedClients =
	            businessRepository
	                    .findByAssignedCA(ca)
	                    .size();

	    long pendingCompliances =
	            complianceRepository
	                    .countByBusinessAssignedCAAndComplianceStatus(
	                            ca,
	                            ComplianceStatus.PENDING
	                    );

	    long overdueCompliances =
	            complianceRepository
	                    .countByBusinessAssignedCAAndComplianceStatus(
	                            ca,
	                            ComplianceStatus.OVERDUE
	                    );

	    LocalDate today = LocalDate.now();

	    LocalDate next7Days = today.plusDays(7);

	    long upcomingDeadlines =
	            complianceRepository
	                    .countByBusinessAssignedCAAndComplianceDueDateBetween(
	                            ca,
	                            today,
	                            next7Days
	                    );

	    return new CADashboardStatsDTO(
	            totalAssignedClients,
	            pendingCompliances,
	            overdueCompliances,
	            upcomingDeadlines
	    );
	}

}
