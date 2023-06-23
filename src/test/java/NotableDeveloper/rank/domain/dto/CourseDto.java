package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.hash;

@Getter
@Setter
public class CourseDto implements Comparable<CourseDto> {
    String title;
    int year;
    Semester semester;
    String code;
    float rating;
    int count;
    float average;
    Tier tier;

    public CourseDto(String title, int year, Semester semester, String code, float rating){
        this.title = title;
        this.year = year;
        this.semester = semester;
        this.code = code;
        this.rating = rating;
        this.count = 1;
        this.average = 0.0F;
        this.tier = Tier.U;
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

    /*
        average 값을 우선으로 내림차순 정렬을 수행.
        average 값이 같다면 count 값을 기준으로 정렬을 수행.
     */
    @Override
    public int compareTo(CourseDto other) {
        int averageComparison = Float.compare(other.getAverage(), this.getAverage());

        if (averageComparison != 0) {
            return averageComparison;
        }
        return Integer.compare(other.getCount(), this.getCount());
    }
}
