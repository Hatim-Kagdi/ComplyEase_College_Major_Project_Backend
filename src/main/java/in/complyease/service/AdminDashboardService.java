package in.complyease.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.complyease.dto.admin.PlatformStatsDTO;
import in.complyease.enums.UserRole;
import in.complyease.repository.*;

@Service
public class AdminDashboardService {

    @Autowired private UserRepository userRepository;

    @Autowired private BusinessRepository businessRepository;

    @Autowired private ComplianceRepository complianceRepository;

    public PlatformStatsDTO getPlatformStats() {

        long totalUsers =
                userRepository.count();

        long totalBusinesses =
                businessRepository.count();

        long totalCompliances =
                complianceRepository.count();

        long totalCAs =
                userRepository.findByRole(
                        UserRole.ROLE_CA
                ).size();

        return new PlatformStatsDTO(
                totalUsers,
                totalBusinesses,
                totalCompliances,
                totalCAs
        );
    }
}