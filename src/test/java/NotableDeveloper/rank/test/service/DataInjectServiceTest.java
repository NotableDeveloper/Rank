package NotableDeveloper.rank.test.service;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.repository.*;
import NotableDeveloper.rank.service.DataInjectService;
import NotableDeveloper.rank.service.SimpleInjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DataInjectServiceTest {
    DataInjectService simpleInjectService;

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

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        simpleInjectService = new SimpleInjectService(
                courseRepository,
                professorRepository,
                departmentRepository,
                courseProfessorRepository,
                rankVersionRepository
        );
    }

    @Test
    @DisplayName("강의 평가 데이터 주입은 오직 한번만 수행되어야 한다.")
    void 데이터_중복저장_방지_테스트(){
        /*
            Given :
            simpleInjectService의 updateEvaluates 메서드는 강의 데이터가
            중복으로 주입되는 경우를 방지하기 위해, RankVersion 이라는 테이블을 만들고
            학년-학기마다 데이터를 한 번에 주입 받도록 한다.

            해당 테스트에서는 2020학년도 1학기 강의 데이터를 주입하는 상황을 가정한다.
            즉, 해당 테스트는 2020학년도 1학기 강의평가 데이터를 주입하는 상황이며 같은 학기를
            또 주입하려고 시도하면 예외가 발생하는 지를 검증하는 테스트이다.
         */
        int year = 2020;
        Semester semester = Semester.FIRST;

        /*
            When :
            updateEvaluates 메서드 내부에서는 rankVersionRepository의
            existsYearAndSemester 메서드를 호출하여 중복 호출 여부를 확인한다.

            RankVersion 테이블에 2020년 1학기 데이터가 주입되어 있지 않은 상태에서
            existsYearAndSemester를 호출하면 false가 반환되어야 한다.
            이후 existsYearAndSemester를 호출하면 2020년 1학기 데이터가 주입되어 있을 것이므로
            true가 반환되어야 한다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemester(year, semester))
                .thenReturn(false)
                .thenReturn(true);

        /*
            Then :
            처음 호출하는 updateEvaluates 메서드는 예외가 발생하지 않고 동작한다.
            이후 호출하는 updateEvaluates 메서드는 예외가 발생하게 된다.
         */
        Assertions.assertDoesNotThrow(() ->
                        simpleInjectService.updateEvaluates(year, semester));

        Assertions.assertThrows(EvaluationAlreadyException.class,
                () -> simpleInjectService.updateEvaluates(year, semester));
    }


}
