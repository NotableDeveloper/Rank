package NotableDeveloper.rank.test.service.CourseService;

import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.service.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

public class CourseServiceTest {
    CourseService courseService;

    @Mock
    CourseProfessorRepository courseProfessorRepository;

    @BeforeEach
    void setUp(){
        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        courseService = new CourseService();
        courseService.setCourseProfessorRepository(courseProfessorRepository);
    }

    @Test
    @DisplayName("특정 강의명으로 검색했을 때, 결과가 없다면 예외가 발생한다.")
    void 강의명_검색_실패_테스트(){
        Mockito.when(courseProfessorRepository.findAllByCourse_Title(Mockito.any()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(CourseNotFoundException.class,
                () -> courseService.getCourseByTitle("객체지향 프로그래밍"));
    }
}
