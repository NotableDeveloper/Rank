package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;

@Builder
public class CourseHistoryDto {
    Long courseId;
    int year;
    Semester semester;
    Tier tier;
}
