package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.*;
import NotableDeveloper.rank.domain.entity.*;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.ClassifyAlreadyException;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.domain.exceptiion.EvaluationNotFoundException;
import NotableDeveloper.rank.repository.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Service
@Getter
@Setter
public class SimpleInjectService{
    CourseRepository courseRepository;

    ProfessorRepository professorRepository;

    DepartmentRepository departmentRepository;

    CourseProfessorRepository courseProfessorRepository;

    RankVersionRepository rankVersionRepository;

    SimpleEvaluationExtract extractor;
    SimpleEvaluationClassify classification;
    public void saveEvaluates(int year, Semester semester) {
        if(rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
            throw new EvaluationAlreadyException();

        RankVersion rankVersion = rankVersionRepository.save(new RankVersion(year, semester));

        extractor.extractEvaluation();

        saveDepartment();
        saveCourse();
        saveProfessor();
        saveCourseProfessor();

        rankVersion.setInjected(true);
        rankVersionRepository.save(rankVersion);
    }

    private void saveDepartment(){
        for(DepartmentDto departmentDto : extractor.getDepartments()){
            String college = departmentDto.getCollege();
            String originalName = departmentDto.getOriginalName();

            if(!departmentRepository.existsByCollegeAndOriginalName(college, originalName)){
                departmentRepository.save(new Department(college, originalName));
            }
        }
    }

    private void saveCourse(){
        for(CourseDto courseDto : extractor.getCourses()){
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

    private void saveProfessor() {
        for (ProfessorDto professorDto : extractor.getProfessors()) {
            if (!professorRepository.existsByNameAndDepartment_OriginalName(
                    professorDto.getName(),
                    professorDto.getDepartment()
            )) {
                Department professorDepartment = departmentRepository.findByCollegeAndOriginalName(
                        professorDto.getCollege(),
                        professorDto.getDepartment());

                professorRepository.save(new Professor(
                        professorDto.getName(),
                        professorDto.getCollege(),
                        professorDepartment,
                        professorDto.getPosition()));
            }
        }
    }

    private void saveCourseProfessor(){
        for(EvaluationDto evaluationDto : extractor.getEvaluations()){
            if(!courseProfessorRepository.existsByCourse_TitleAndProfessor_Name(
                    evaluationDto.getTitle(),
                    evaluationDto.getProfessorName()
            )){
                Course course = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                        evaluationDto.getTitle(),
                        evaluationDto.getYear(),
                        evaluationDto.getSemester(),
                        evaluationDto.getCode().substring(0, 8)
                );

                Professor professor = professorRepository.findByNameAndDepartment_OriginalName(
                        evaluationDto.getProfessorName(),
                        evaluationDto.getDepartment()
                );

                courseProfessorRepository.save(new CourseProfessor(course, professor));
            }
        }
    }

    public void updateEvaluates(int year, Semester semester){
        if(!rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
            throw new EvaluationNotFoundException();

        if(rankVersionRepository.existsByYearAndSemesterAndCalculatedIsTrue(year, semester))
            throw new ClassifyAlreadyException();

        RankVersion rankVersion = rankVersionRepository.findByYearAndSemester(year, semester);
        rankVersion.setCalculated(true);
        rankVersionRepository.save(rankVersion);
    }
}
