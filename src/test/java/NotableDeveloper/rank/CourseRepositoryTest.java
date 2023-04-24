package NotableDeveloper.rank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    @Test
    public void 강의_정상등록_테스트() {
        final String title = "객체지향 프로그래밍";
        final int offeredYear = 2023;
        final Semester semester = Semester.FIRST;
        final String code = "1234567890";

        final Course course = new Course(title, offeredYear, semester, code);

        courseRepository.save(course);

        final Course findCourse = courseRepository.findByTitleAndOfferedYearAndSemester(title, offeredYear, semester);

        Assertions.assertEquals("객체지향 프로그래밍", findCourse.getTitle());
        Assertions.assertEquals(2023, findCourse.getOfferedYear());
        Assertions.assertEquals(Semester.FIRST, findCourse.getSemester());
        Assertions.assertEquals(code, findCourse.getCode());
        Assertions.assertEquals(0.0F, findCourse.getRating());
        Assertions.assertEquals(Tier.F, findCourse.getTier());
        Assertions.assertEquals(1, findCourse.getCount());
    }
}
