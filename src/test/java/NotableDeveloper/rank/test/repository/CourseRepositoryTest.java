package NotableDeveloper.rank.test.repository;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    static RankData data = new RankData();

    @Test
    @DisplayName("강의를 하나 등록하고, 제대로 DB에 저장되었는 지를 검증한다.")
    public void 강의_정상등록_테스트() {
        Course course = data.getCourses().get(0);
        courseRepository.save(course);

        final Course findCourse = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                course.getTitle(),
                course.getOfferedYear(),
                course.getSemester(),
                course.getCode()
        );

        Assertions.assertEquals(course, findCourse);
    }
}
