package NotableDeveloper.rank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    @Test
    public void 강의_정상등록_테스트(){
        final String title = "객체지향 프로그래밍";
        final int year = 2023;
        final Semester semester = Semester.FIRST;

        final Course course = Course.builder()
                .title(title)
                .year(year)
                .semester(semester)
                .code("12345678")
                .rating(95.50F)
                .tier(Tier.A)
                .build();

        courseRepository.save(course);

        final Course findCourse = courseRepository.findByTitleAndYearAndSemester(title, year, semester);

        Assertions.assertEquals("객체지향 프로그래밍", findCourse.getTitle());
        Assertions.assertEquals(2023, findCourse.getYear());
        Assertions.assertEquals(Semester.FIRST, findCourse.getSemester());
    }
}
