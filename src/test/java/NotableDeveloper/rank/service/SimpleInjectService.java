package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.DepartmentDto;
import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.dto.ShortenDepartmentDto;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.RankVersion;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.repository.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        if(rankVersionRepository.existsByYearAndSemester(year, semester))
            throw new EvaluationAlreadyException();

        rankVersionRepository.save(new RankVersion(year, semester));

        for(EvaluationDto evaluationDto : evaluations){
            DepartmentDto departmentDto =
                    new DepartmentDto(evaluationDto.getCollege(), evaluationDto.getDepartment());

            departmentSet.add(departmentDto);
        }

        updateDepartment();


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

    @Override
    public void updateDepartmentShorten(List<ShortenDepartmentDto> simpleDepartments) {

    }

    public void hello(){
        System.out.println("Hello!");
    }
}
