package NotableDeveloper.rank.test.service.SimpleInjectService;

import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.exceptiion.ClassifyAlreadyException;
import NotableDeveloper.rank.domain.entity.RankVersion;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationNotFoundException;
import NotableDeveloper.rank.repository.*;
import NotableDeveloper.rank.service.SimpleEvaluationExtract;
import NotableDeveloper.rank.service.SimpleInjectService;
import NotableDeveloper.rank.test.data.SampleCsvExtract;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    final int year = 2023;
    final Semester semester = Semester.FIRST;
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

        /*
            메서드 테스트 과정에서 무조건 rankVersionRepository의 existsByYearAndSemesterAndInjectedIsTrue 메서드와
            existsByYearAndSemesterAndCalculatedIsTrue 메서드, save 메서드가 호출되므로 이에 대한 설정을 해둔다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
                .thenReturn(true)
                .thenReturn(false);

        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndCalculatedIsTrue(year, semester))
                .thenReturn(false)
                .thenReturn(true);

        Mockito.when(rankVersionRepository.findByYearAndSemester(year, semester))
                .thenReturn(new RankVersion(year, semester));

        Mockito.when(rankVersionRepository.save(Mockito.any()))
                .thenReturn(new RankVersion(year, semester));
    }
    @Test
    @DisplayName("등급을 부여하기 이전에 강의 정보가 주입되어 있지 않으면 예외가 발생한다.")
    void 강의정보_주입되지_않은경우_예외발생_테스트(){
        /*
            updateEvaluates()를 호출할 때, 사전에 강의 정보가 주입되어 있지 않으면
            예외가 발생한다.
         */
        Assertions.assertDoesNotThrow(() ->
                simpleInjectService.updateEvaluates(year, semester));

        Assertions.assertThrows(EvaluationNotFoundException.class,
                () -> simpleInjectService.updateEvaluates(year, semester));
    }

    @Test
    @DisplayName("강의, 교수 데이터에 등급을 부여하는 중복하여 수행하면 예외가 발생한다.")
    void 티어부여_중복방지_테스트(){
        /*
            updateEvaluates()가 같은 학기에 두 번 호출 되었고, 첫 번째 호출에서
            성공적으로 강의평가 데이터를 주입하였다면 두 번째 호출에서는 예외가 발생해야 한다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
                .thenReturn(true);

        Assertions.assertDoesNotThrow(() ->
                simpleInjectService.updateEvaluates(year, semester));

        Assertions.assertThrows(ClassifyAlreadyException.class,
                () -> simpleInjectService.updateEvaluates(year, semester));
    }
}
