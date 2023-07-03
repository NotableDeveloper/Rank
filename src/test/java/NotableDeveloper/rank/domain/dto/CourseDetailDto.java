package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.hash;

@Getter
@Setter
@Builder
public class CourseDetailDto {
    Long courseId;
    String code;
    String title;
    int year;
    Semester semester;
    Tier tier;
    ProfessorDetailDto professor;

    @Override
    public boolean equals(Object object){
        CourseDetailDto course = (CourseDetailDto) object;

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
