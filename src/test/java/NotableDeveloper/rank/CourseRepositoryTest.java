package NotableDeveloper.rank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    @Test
    public void 강의_정상등록_테스트(){
        final String title = "객체지향 프로그래밍";
        final int offeredYear = 2023;
        final Semester semester = Semester.FIRST;

        final Course course = new Course(1L, title, offeredYear, semester, "12345678", 95.50F, Tier.A);

        courseRepository.save(course);

        final Course findCourse = courseRepository.findByTitleAndOfferedYearAndSemester(title, offeredYear, semester);

        Assertions.assertEquals("객체지향 프로그래밍", findCourse.getTitle());
        Assertions.assertEquals(2023, findCourse.getOfferedYear());
        Assertions.assertEquals(Semester.FIRST, findCourse.getSemester());
    }
}
