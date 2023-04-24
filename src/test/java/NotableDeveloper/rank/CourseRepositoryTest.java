package NotableDeveloper.rank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    static ArrayList<Course> courses;

    @BeforeEach
    @DisplayName("3개의 분반이 개설된 객체지향 프로그래밍 강의를 준비한다")
    void setUp(){
        courses = new ArrayList<>();

        courses.add(new Course("객체지향 프로그래밍", 2023, Semester.FIRST, "12345678", 79.00F));
        courses.add(new Course("객체지향 프로그래밍", 2023, Semester.FIRST, "12345678", 76.00F));
        courses.add(new Course("객체지향 프로그래밍", 2023, Semester.FIRST, "12345678", 82.00F));
    }
    @Test
    @DisplayName("객체지향 프로그래밍 강의를 하나 등록한다")
    public void 강의_정상등록_테스트() {
        Course targetCourse = courses.get(0);

        courseRepository.save(targetCourse);

        final Course findCourse = courseRepository.findByTitleAndOfferedYearAndSemester("객체지향 프로그래밍", 2023, Semester.FIRST);

        Assertions.assertEquals("객체지향 프로그래밍", findCourse.getTitle());
        Assertions.assertEquals(2023, findCourse.getOfferedYear());
        Assertions.assertEquals(Semester.FIRST, findCourse.getSemester());
        Assertions.assertEquals("12345678", findCourse.getCode());
        Assertions.assertEquals(0.0F, findCourse.getRating());
        Assertions.assertEquals(Tier.F, findCourse.getTier());
        Assertions.assertEquals(1, findCourse.getCount());
    }

    @Test
    @DisplayName("여러 분반으로 개설되는 강의는 DB에 하나만 등록되어야 한다")
    public void 여러분반_강의등록_테스트(){
        for(Course course : courses) {
            if (courseRepository.existsByTitleAndOfferedYearAndSemesterAndCode(
                    course.getTitle(),
                    course.getOfferedYear(),
                    course.getSemester(),
                    course.getCode()
            )){
                Course updateCourse = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                        course.getTitle(),
                        course.getOfferedYear(),
                        course.getSemester(),
                        course.getCode()
                );

                int updateCount = updateCourse.getCount() + 1;
                float updateRating = updateCourse.getRating() + course.getRating();

                updateCourse.setCount(updateCount);
                updateCourse.setRating(updateRating);

                courseRepository.save(updateCourse);
            }

            else courseRepository.save(course);
        }

        Course course = courses.get(0);

        Course findCourse = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                course.getTitle(),
                course.getOfferedYear(),
                course.getSemester(),
                course.getCode()
        );

        Assertions.assertEquals("객체지향 프로그래밍", findCourse.getTitle());
        Assertions.assertEquals(2023, findCourse.getOfferedYear());
        Assertions.assertEquals(Semester.FIRST, findCourse.getSemester());
        Assertions.assertEquals("12345678", findCourse.getCode());
        Assertions.assertEquals(237.0F, findCourse.getRating());
        Assertions.assertEquals(Tier.F, findCourse.getTier());
        Assertions.assertEquals(3, findCourse.getCount());
    }
}
