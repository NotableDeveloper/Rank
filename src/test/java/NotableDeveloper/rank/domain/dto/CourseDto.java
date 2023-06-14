package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.hash;

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

    @Override
    public boolean equals(Object object){
        CourseDto course = (CourseDto) object;

        return this.getTitle() == course.getTitle() &&
                this.getCode() == course.getCode() &&
                this.getYear() == course.getYear() &&
                this.getSemester() == course.getSemester();
    }

    @Override
    public int hashCode(){

        return hash(this.getTitle(),
                this.getCode(),
                this.getYear(),
                this.getSemester());
    }
}
