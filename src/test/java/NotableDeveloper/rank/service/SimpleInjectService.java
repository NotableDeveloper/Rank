package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.DepartmentDto;
import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.dto.ShortenDepartmentDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.RankVersion;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.repository.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class SimpleInjectService implements DataInjectService {
    CourseRepository courseRepository;

    ProfessorRepository professorRepository;

    DepartmentRepository departmentRepository;

    CourseProfessorRepository courseProfessorRepository;

    RankVersionRepository rankVersionRepository;

    private HashSet<DepartmentDto> departmentSet;

    @Override
    public void updateEvaluates(int year, Semester semester, List<EvaluationDto> evaluations) {
        ArrayList<CourseDto> courses = new ArrayList<>();

        if(rankVersionRepository.existsByYearAndSemester(year, semester))
            throw new EvaluationAlreadyException();

        rankVersionRepository.save(new RankVersion(year, semester));

        for(EvaluationDto evaluationDto : evaluations){
            DepartmentDto departmentDto =
                    new DepartmentDto(evaluationDto.getCollege(), evaluationDto.getDepartment());

            departmentSet.add(departmentDto);

            courses.add(new CourseDto(
                    evaluationDto.getTitle(),
                    evaluationDto.getYear(),
                    evaluationDto.getSemester(),
                    evaluationDto.getCode(),
                    evaluationDto.getRating()));
        }

        updateDepartment();
        updateCourse(courses);
    }

    private void updateDepartment(){
        for(DepartmentDto departmentDto : departmentSet){
            String college = departmentDto.getCollege();
            String originalName = departmentDto.getOriginalName();

            if(!departmentRepository.existsByCollegeAndOriginalName(college, originalName)){
                departmentRepository.save(new Department(college, originalName));
            }
        }
    }

    private void updateCourse(List<CourseDto> courses){
        for(CourseDto courseDto : courses){
            String slicedCode = courseDto.getCode().substring(0, 8);

            if(!courseRepository.existsByTitleAndOfferedYearAndSemesterAndCode(
                    courseDto.getTitle(),
                    courseDto.getYear(),
                    courseDto.getSemester(),
                    slicedCode)){

                courseRepository.save(new Course(
                        courseDto.getTitle(),
                        courseDto.getYear(),
                        courseDto.getSemester(),
                        slicedCode,
                        courseDto.getRating()
                ));
            }

            else{
                Course updateCourse = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                        courseDto.getTitle(),
                        courseDto.getYear(),
                        courseDto.getSemester(),
                        slicedCode);

                int updateCount = updateCourse.getCount() + 1;
                float updateRating = updateCourse.getRating() + courseDto.getRating();

                updateCourse.setCount(updateCount);
                updateCourse.setRating(updateRating);

                courseRepository.save(updateCourse);
            }
        }
    }

    @Override
    public void updateDepartmentShorten(List<ShortenDepartmentDto> simpleDepartments) {

    }
}
