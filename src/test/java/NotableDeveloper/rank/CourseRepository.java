package NotableDeveloper.rank;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByTitleAndOfferedYearAndSemester(String title, int year, Semester semester);
    boolean existsByTitleAndOfferedYearAndSemesterAndCode(String title, int offeredYear, Semester semester, String code);

}
