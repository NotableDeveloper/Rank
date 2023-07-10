package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@Builder
public class ProfessorDetailDto {
    Long professorId;
    String name;
    String college;
    String department;
    String position;
    Tier professorTier;
    int courseCount;
    List<CourseHistoryDto> offeredCourses;
}
