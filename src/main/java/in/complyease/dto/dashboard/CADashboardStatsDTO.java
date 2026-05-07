package in.complyease.dto.dashboard;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CADashboardStatsDTO {
    private long totalAssignedClients;

    private long pendingCompliances;

    private long overdueCompliances;

    private long upcomingDeadlines;

}
