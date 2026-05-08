package in.complyease.dto.admin;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlatformStatsDTO {

    private long totalUsers;

    private long totalBusinesses;

    private long totalCompliances;

    private long totalCAs;
}