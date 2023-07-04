package NotableDeveloper.rank.service.implement;

import NotableDeveloper.rank.domain.dto.*;
import NotableDeveloper.rank.service.function.EvaluationExtract;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class SimpleEvaluationExtract implements EvaluationExtract {
    private List<EvaluationDto> evaluations;
    private List<CourseDataDto> courses;
    private List<ProfessorDto> professors;
    private Set<DepartmentDataDto> departments;
    private Map<String, String> shortenDepartments;

    public SimpleEvaluationExtract(){
        departments = new HashSet<>();
        shortenDepartments = new HashMap();
    }

    public Set<DepartmentDataDto> getDepartments() {
        return this.departments;
    }

    @Override
    public void extractEvaluation() {
        courses = new ArrayList<>();
        professors = new ArrayList<>();

        for(EvaluationDto evaluationDto : evaluations){
            departments.add(DepartmentDataDto.builder()
                    .college(evaluationDto.getCollege())
                    .originalName(evaluationDto.getDepartment())
                    .build());

            courses.add(CourseDataDto.builder()
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

    @Override
    public void extractShortDepartments() {
        /*
        To do : extract to file or JSON
         */
    }
}
