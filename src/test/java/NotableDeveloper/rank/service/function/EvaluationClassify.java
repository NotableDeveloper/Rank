package NotableDeveloper.rank.service.function;

import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;

import java.util.List;

public interface EvaluationClassify {
    public void classifyCourse(List<CourseDto> courses);

    public void classifyProfessor(List<ProfessorDto> professors);
}
