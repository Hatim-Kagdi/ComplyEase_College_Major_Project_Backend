package in.complyease.dto.dashboard;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDashboardStatsDTO {
	private long totalBusinesses;

    private long pendingCompliances;

    private long completedCompliances;

    private long upcomingDueDates;

}
