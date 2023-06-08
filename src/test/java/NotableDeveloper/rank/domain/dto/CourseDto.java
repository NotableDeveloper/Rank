package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseDto {
    String title;
    int year;
    Semester semester;
    String code;
    float rating;
}
