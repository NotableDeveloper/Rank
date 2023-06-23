package NotableDeveloper.rank.service.implement;

import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.DepartmentDto;
import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.service.function.EvaluationExtract;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SimpleEvaluationExtract implements EvaluationExtract {
    private List<EvaluationDto> evaluations;
    private List<CourseDto> courses;
    private List<ProfessorDto> professors;
    private Set<DepartmentDto> departments;

    public SimpleEvaluationExtract(){
        departments = new HashSet<>();
    }

    public Set<DepartmentDto> getDepartments() {
        return this.departments;
    }

    @Override
    public void extractEvaluation() {
        courses = new ArrayList<>();
        professors = new ArrayList<>();

        for(EvaluationDto evaluationDto : evaluations){
            departments.add(new DepartmentDto(
                    evaluationDto.getCollege(),
                    evaluationDto.getDepartment()));

            courses.add(new CourseDto(
                    evaluationDto.getTitle(),
                    evaluationDto.getYear(),
                    evaluationDto.getSemester(),
                    evaluationDto.getCode(),
                    evaluationDto.getRating()));

            professors.add(new ProfessorDto(
                    evaluationDto.getProfessorName(),
                    evaluationDto.getCollege(),
                    evaluationDto.getDepartment(),
                    evaluationDto.getPosition()
            ));
        }
    }
}
