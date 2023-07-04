package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.CourseDetailDto;
import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.CourseHistoryDto;
import NotableDeveloper.rank.domain.dto.ProfessorDetailDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.CourseRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseService {
    CourseProfessorRepository courseProfessorRepository;
    CourseRepository courseRepository;

    public List<CourseDto> getCourseByTitle(String title){
        List<CourseDto> courses = new ArrayList<>();
        ArrayList<CourseProfessor> courseProfessors = courseProfessorRepository.findAllByCourse_TitleContains(title);

        if(courseProfessors.size() <= 0)
            throw new CourseNotFoundException();

        courseProfessors.forEach(cp -> {
            Professor p = cp.getProfessor();
            Course c = cp.getCourse();

            courses.add(CourseDto.builder()
                    .courseId(c.getId())
                    .title(c.getTitle())
                    .year(c.getOfferedYear())
                    .semester(c.getSemester())
                    .tier(c.getTier())
                    .professor(p.getName())
                    .department(p.getDepartment().getOriginalName())
                    .build());
        });

        return courses;
    }

    public CourseDetailDto getCourseById(Long courseId){
        CourseProfessor cpByCourseId = courseProfessorRepository.findByCourse_Id(courseId);

        if(cpByCourseId == null) throw new CourseNotFoundException();

        Course c = cpByCourseId.getCourse();
        Professor p = cpByCourseId.getProfessor();

        ProfessorDetailDto professorDetailDto = ProfessorDetailDto.builder()
                .name(p.getName())
                .professorId(p.getId())
                .department(p.getDepartment().getOriginalName())
                .college(p.getCollege())
                .position(p.getPosition())
                .tier(p.getTier())
                .build();

        List<CourseHistoryDto> history = getPreviousCourseHistory(c.getCode());

        return CourseDetailDto.builder()
                .title(c.getTitle())
                .year(c.getOfferedYear())
                .semester(c.getSemester())
                .courseId(c.getId())
                .courseTier(c.getTier())
                .professor(professorDetailDto)
                .history(history)
                .build();
    }

    private List<CourseHistoryDto> getPreviousCourseHistory(String code){
        List<Course> coursesByCode = courseRepository.findAllByCode(code);
        List<CourseHistoryDto> history = new ArrayList<>();

        coursesByCode.forEach(c -> {
            history.add(CourseHistoryDto.builder()
                    .courseId(c.getId())
                    .year(c.getOfferedYear())
                    .semester(c.getSemester())
                    .tier(c.getTier())
                    .build());
        });

        return history;
    }
}
