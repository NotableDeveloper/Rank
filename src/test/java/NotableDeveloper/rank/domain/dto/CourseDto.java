package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {
    String title;
    int year;
    Semester semester;
    String code;
    float rating;
    int count;

    public CourseDto(String title, int year, Semester semester, String code, float rating){
        this.title = title;
        this.year = year;
        this.semester = semester;
        this.code = code;
        this.rating = rating;
        count = 1;
    }
}
