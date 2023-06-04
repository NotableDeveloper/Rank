package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EvaluationDto {
    int year;
    Semester semester;
    String code;
    String title;
    String professorName;
    String college;
    String department;
    String position;
}
