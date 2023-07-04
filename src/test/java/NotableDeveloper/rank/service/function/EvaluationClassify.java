package NotableDeveloper.rank.service.function;

import NotableDeveloper.rank.domain.dto.CourseDataDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;

import java.util.List;

public interface EvaluationClassify {
    public void classifyCourse(List<CourseDataDto> courses);

    public void classifyProfessor(List<ProfessorDto> professors);
}
