package NotableDeveloper.rank;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByTitleAndYearAndSemester(String title, int year, Semester semester);
}
