package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CourseDetailDto {
    Long courseId;
    String code;
    String title;
    int year;
    Semester semester;
    Tier courseTier;
    ProfessorDetailDto professor;
    List<CourseHistoryDto> history;
}
