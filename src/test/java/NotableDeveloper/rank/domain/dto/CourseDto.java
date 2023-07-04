package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;

@Builder
public class CourseDto {
    Long courseId;
    String title;
    int year;
    Semester semester;
    Tier tier;
    String professor;
    String department;
}
