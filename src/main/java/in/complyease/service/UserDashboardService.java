package in.complyease.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.complyease.dto.UserDashboardStatsDTO;
import in.complyease.entity.Business;
import in.complyease.entity.Compliance;
import in.complyease.entity.User;
import in.complyease.enums.ComplianceStatus;
import in.complyease.repository.BusinessRepository;
import in.complyease.repository.ComplianceRepository;
import in.complyease.repository.UserRepository;

@Service
public class UserDashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ComplianceRepository complianceRepository;

    public UserDashboardStatsDTO getUserDashboardStats(String email) {

        // USER
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // BUSINESSES
        List<Business> businesses =
                businessRepository.findByUser(user);

        long totalBusinesses = businesses.size();

        // COMPLIANCES
        List<Compliance> compliances =
                complianceRepository.findByBusinessIn(businesses);

        // PENDING
        long pendingCompliances =
                compliances.stream()
                        .filter(c ->
                                c.getComplianceStatus() ==
                                ComplianceStatus.PENDING
                        )
                        .count();

        // COMPLETED
        long completedCompliances =
                compliances.stream()
                        .filter(c ->
                                c.getComplianceStatus() ==
                                ComplianceStatus.COMPLETED
                        )
                        .count();

        // UPCOMING DUE DATES
        LocalDate today = LocalDate.now();

        long upcomingDueDates =
                compliances.stream()
                        .filter(c ->
                                c.getComplianceDueDate()
                                        .isAfter(today)
                        )
                        .count();

        return new UserDashboardStatsDTO(
                totalBusinesses,
                pendingCompliances,
                completedCompliances,
                upcomingDueDates
        );
    }
}