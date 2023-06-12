package NotableDeveloper.rank.test.repository;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;


@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    static RankData data = new RankData();

    @Test
    @DisplayName("객체지향 프로그래밍 강의를 하나 등록한다")
    public void 강의_정상등록_테스트() {
        Course course = data.getCourses().get(0);
        courseRepository.save(course);

        final Course findCourse = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                "객체지향 프로그래밍",
                2023,
                Semester.FIRST,
                "12345678"
                );

        Assertions.assertEquals("객체지향 프로그래밍", findCourse.getTitle());
        Assertions.assertEquals(2023, findCourse.getOfferedYear());
        Assertions.assertEquals(Semester.FIRST, findCourse.getSemester());
        Assertions.assertEquals("12345678", findCourse.getCode());
        Assertions.assertEquals(79.0F, findCourse.getRating());
        Assertions.assertEquals(Tier.F, findCourse.getTier());
        Assertions.assertEquals(1, findCourse.getCount());
    }

    @Test
    @DisplayName("여러 분반으로 개설되는 강의는 DB에 하나만 등록되어야 한다")
    public void 여러분반_강의등록_테스트(){
        ArrayList<Course> courses = data.getCourses();

        /*
            현재 courses에 저장된 강의를 모두 등록한다.
            객체지향 프로그래밍 강의가 3개의 분반으로 등록되어 있으니 DB에는 1개의 강의 데이터만
            저장되어 있고, 그 데이터의 count 값은 3이고 rating은 3개 강의의 합계이어야 한다.
         */
        for(Course course : courses) {
            /*
                동일한 강의명, 년도, 학기를 가진 데이터가 이미 DB에 있다면 해당 강의는 분반으로 개설된 것이다.
                그러한 강의를 찾았다면 count 값과 rating 값을 갱신해준다.
             */
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
        /*
            객체지향 프로그래밍 강의가 3개의 분반으로 개설되었으므로, 이에 대한 검증을 수행한다.
         */
        Course course = courses.get(0);

        Course findCourse = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                course.getTitle(),
                course.getOfferedYear(),
                course.getSemester(),
                course.getCode()
        );

        /*
            DB 상에 저장된 "객체지향 프로그래밍" 이라는 이름의 강의 데이터가 총 몇 개인지를 카운팅한다.
            정상적으로 등록되었다면 현재 DB에는 3개의 분반이 하나의 데이터로 저장되어 있어야한다.
         */
        int count = (int) courseRepository.findAll().stream().filter(c -> c.getTitle().contains("객체지향 프로그래밍")).count();
        Assertions.assertEquals(1, count);

        Assertions.assertEquals("객체지향 프로그래밍", findCourse.getTitle());
        Assertions.assertEquals(2023, findCourse.getOfferedYear());
        Assertions.assertEquals(Semester.FIRST, findCourse.getSemester());
        Assertions.assertEquals("12345678", findCourse.getCode());
        Assertions.assertEquals(237.0F, findCourse.getRating());
        Assertions.assertEquals(Tier.F, findCourse.getTier());
        Assertions.assertEquals(3, findCourse.getCount());
    }
}
