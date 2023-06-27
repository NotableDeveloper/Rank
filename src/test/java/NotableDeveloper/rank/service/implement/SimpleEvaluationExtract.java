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
            departments.add(DepartmentDto.builder()
                    .college(evaluationDto.getCollege())
                    .originalName(evaluationDto.getDepartment())
                    .build());

            courses.add(CourseDto.builder()
                    .title(evaluationDto.getTitle())
                    .year(evaluationDto.getYear())
                    .semester(evaluationDto.getSemester())
                    .code(evaluationDto.getCode())
                    .rating(evaluationDto.getRating())
                    .build());

            professors.add(ProfessorDto.builder()
                    .name(evaluationDto.getProfessorName())
                    .college(evaluationDto.getCollege())
                    .department(evaluationDto.getDepartment())
                    .position(evaluationDto.getPosition())
                    .build());
        }
    }
}
