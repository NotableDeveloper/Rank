package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByTitleAndOfferedYearAndSemesterAndCode(String title, int year, Semester semester, String code);
    boolean existsByTitleAndOfferedYearAndSemesterAndCode(String title, int offeredYear, Semester semester, String code);
    @Query("SELECT c FROM Course c WHERE c.offeredYear < :year OR (c.offeredYear = :year AND c.semester = :semester)")
    List<Course> findAllPreviousOrSameVersions(int year, Semester semester);
}
