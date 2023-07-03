package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.CourseDetailDto;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseService {
    CourseProfessorRepository courseProfessorRepository;

    List<CourseDetailDto> getCourseByTitle(String title){
        List<CourseDetailDto> courses = new ArrayList<>();
        return courses;
    }
}
