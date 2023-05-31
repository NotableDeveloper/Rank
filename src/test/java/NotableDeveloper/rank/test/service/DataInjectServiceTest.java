package NotableDeveloper.rank.test.service;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.service.DataInjectService;
import NotableDeveloper.rank.service.SimpleInjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DataInjectServiceTest {
    DataInjectService dataInjectService;

    @BeforeEach
    void setUp(){
        dataInjectService = new SimpleInjectService();
    }

    @Test
    @DisplayName("강의 평가 데이터 주입은 오직 한번만 수행되어야 한다.")
    void 데이터_중복저장_방지_테스트(){
        int year = 2020;
        Semester semester = Semester.FIRST;
        dataInjectService.updateEvaluates(year, semester);

        Assertions.assertThrows(EvaluationAlreadyException.class,
                () -> dataInjectService.updateEvaluates(year, semester));

    }
}
