package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.*;
import NotableDeveloper.rank.domain.entity.*;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.ClassifyAlreadyException;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.domain.exceptiion.EvaluationNotFoundException;
import NotableDeveloper.rank.repository.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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

    public void updateCourses(int year, Semester semester){
        if(!rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
            throw new EvaluationNotFoundException();

        if(rankVersionRepository.existsByYearAndSemesterAndClassifiedCourseIsTrue(year, semester))
            throw new ClassifyAlreadyException();

        List<CourseDto> courses = courseRepository.findAllPreviousOrSameVersions(year, semester)
                .stream()
                .map(course ->
                    new CourseDto(
                            course.getTitle(),
                            course.getOfferedYear(),
                            course.getSemester(),
                            course.getCode(),
                            course.getRating()
                    )
                ).collect(Collectors.toList());

        classification.classifyCourse(courses);

        courses = classification.getUniqueCourses();

        courses.forEach(courseDto -> {
            Course updateCourse = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                    courseDto.getTitle(),
                    courseDto.getYear(),
                    courseDto.getSemester(),
                    courseDto.getCode()
            );

            updateCourse.setRating(courseDto.getAverage());
            updateCourse.setTier(courseDto.getTier());

            courseRepository.save(updateCourse);
        });

        List<RankVersion> rankVersions = rankVersionRepository.findPreviousOrSameVersions(year, semester);
        rankVersions.forEach(rankVersion -> rankVersion.setClassifiedCourse(true));
        rankVersionRepository.saveAll(rankVersions);
    }

    public void updateProfessors(int year, Semester semester){
        if(!rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
            throw new EvaluationNotFoundException();

        if(rankVersionRepository.existsByYearAndSemesterAndClassifiedProfessorIsTrue(year, semester))
            throw new ClassifyAlreadyException();

        List<ProfessorDto> professors = professorRepository.findAll()
                .stream()
                .map(professor -> {
                    List<CourseProfessor> courseProfessors = courseProfessorRepository.findAllByProfessor_Id(professor.getId());

                    ProfessorDto p = new ProfessorDto(
                            professor.getName(),
                            professor.getCollege(),
                            professor.getDepartment().getOriginalName(),
                            professor.getPosition());

                    courseProfessors.forEach(cp -> {
                        int courseCount = cp.getCourse().getCount();
                        float courseRating = cp.getCourse().getRating();
                        p.setCount(p.getCount() + courseCount);
                        p.setRating(p.getRating() + courseRating);
                    });

                    return p;
                }).collect(Collectors.toList());

        classification.classifyProfessor(professors);

        professors = classification.getUniqueProfessors();

        professors.forEach(professor -> {
            Professor updateProfessor = professorRepository.findByNameAndDepartment_OriginalName(
                    professor.getName(),
                    professor.getDepartment());

            updateProfessor.setCount(professor.getCount());
            updateProfessor.setRating(professor.getRating());
            updateProfessor.setAverage(professor.getAverage());
            updateProfessor.setTier(professor.getTier());

            professorRepository.save(updateProfessor);
        });

        List<RankVersion> rankVersions = rankVersionRepository.findPreviousOrSameVersions(year, semester);
        rankVersions.forEach(rankVersion -> rankVersion.setClassifiedProfessor(true));
        rankVersionRepository.saveAll(rankVersions);
    }
}
