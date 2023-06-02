package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByTitleAndOfferedYearAndSemester(String title, int year, Semester semester);
    Course findByTitleAndOfferedYearAndSemesterAndCode(String title, int year, Semester semester, String code);
    boolean existsByTitleAndOfferedYearAndSemesterAndCode(String title, int offeredYear, Semester semester, String code);

}
