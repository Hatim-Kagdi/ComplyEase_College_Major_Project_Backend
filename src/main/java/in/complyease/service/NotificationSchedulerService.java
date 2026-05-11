package in.complyease.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.complyease.entity.Business;
import in.complyease.entity.Compliance;
import in.complyease.entity.User;
import in.complyease.enums.ComplianceStatus;
import in.complyease.repository.ComplianceRepository;

@Service
public class NotificationSchedulerService {

    @Autowired
    private ComplianceRepository complianceRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    // RUN EVERY DAY AT 9 AM
    @Scheduled(cron = "0 0 9 * * *")
    //@Scheduled(fixedRate = 120000) //For testing it sends email every 2 minutes
    @Transactional
    public void sendComplianceReminders() {
    	System.out.println("sendComplianceReminder");
        LocalDate today = LocalDate.now();
        LocalDate next3Days = today.plusDays(3);
        List<Compliance> compliances = complianceRepository
        		.findByComplianceDueDateBetweenAndComplianceStatusNot(today,next3Days,ComplianceStatus.COMPLETED);

        for (Compliance compliance : compliances) {
            Business business = compliance.getBusiness();
            User user = business.getUser();
            String message =
                    compliance.getComplianceType()
                    + " compliance for "
                    + business.getBusinessName()
                    + " is due on "
                    + compliance.getComplianceDueDate();
            
            if (business.getAssignedCA() != null) {

                emailService.sendEmail(
                        business.getAssignedCA().getEmail(),
                        "Compliance Reminder",
                        "Compliance "
                        + compliance.getComplianceType()
                        + " for business "
                        + business.getBusinessName()
                        + " is due on "
                        + compliance.getComplianceDueDate()
                );
            }

            // SAVE NOTIFICATION IN DATABASE
            notificationService.createNotification(business,message);

            // SEND REAL EMAIL
            emailService.sendEmail(user.getEmail(),"Compliance Reminder",message);
        }
    }
}