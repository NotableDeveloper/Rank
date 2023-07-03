package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.CourseDetailDto;
import NotableDeveloper.rank.domain.dto.ProfessorDetailDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseService {
    CourseProfessorRepository courseProfessorRepository;

    public List<CourseDetailDto> getCourseByTitle(String title){
        List<CourseDetailDto> courses = new ArrayList<>();
        ArrayList<CourseProfessor> courseProfessors = courseProfessorRepository.findAllByCourse_Title(title);

        if(courseProfessors.size() <= 0)
            throw new CourseNotFoundException();

        courseProfessors.forEach(cp -> {
            Professor p = cp.getProfessor();
            Course c = cp.getCourse();

            ProfessorDetailDto professor = ProfessorDetailDto.builder()
                    .professorId(p.getId())
                    .name(p.getName())
                    .college(p.getCollege())
                    .department(p.getDepartment().getOriginalName())
                    .position(p.getPosition())
                    .tier(p.getTier())
                    .build();

            courses.add(CourseDetailDto.builder()
                    .courseId(c.getId())
                    .title(c.getTitle())
                    .code(c.getCode())
                    .year(c.getOfferedYear())
                    .semester(c.getSemester())
                    .tier(c.getTier())
                    .professor(professor)
                    .build());
        });

        return courses;
    }
}
