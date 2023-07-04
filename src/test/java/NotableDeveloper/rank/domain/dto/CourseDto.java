package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CourseDto {
    Long courseId;
    String title;
    int year;
    Semester semester;
    Tier tier;
    String professor;
    String department;
}
