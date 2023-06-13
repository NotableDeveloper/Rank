package NotableDeveloper.rank.test.service.SimpleInjectService;

import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.repository.*;
import NotableDeveloper.rank.service.SimpleEvaluationExtract;
import NotableDeveloper.rank.service.SimpleInjectService;
import NotableDeveloper.rank.test.data.SampleCsvExtract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

public class DataClassifyTest {
    SimpleInjectService simpleInjectService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    ProfessorRepository professorRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    CourseProfessorRepository courseProfessorRepository;
    @Mock
    RankVersionRepository rankVersionRepository;
    ArrayList<EvaluationDto> evaluations;

    @BeforeEach
    void setUp(){
        courseRepository = Mockito.mock(CourseRepository.class);
        professorRepository = Mockito.mock(ProfessorRepository.class);
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        rankVersionRepository = Mockito.mock(RankVersionRepository.class);

        SampleCsvExtract  sampleCsv = new SampleCsvExtract();
        evaluations = sampleCsv.extractEvaluation();
        SimpleEvaluationExtract extract = new SimpleEvaluationExtract();
        extract.setEvaluations(evaluations);

        simpleInjectService = new SimpleInjectService();
        simpleInjectService.setCourseRepository(courseRepository);
        simpleInjectService.setProfessorRepository(professorRepository);
        simpleInjectService.setCourseProfessorRepository(courseProfessorRepository);
        simpleInjectService.setDepartmentRepository(departmentRepository);
        simpleInjectService.setRankVersionRepository(rankVersionRepository);

        //simpleInjectService.setClassification();
    }

    @Test
    void TestMethod(){

    }
}
