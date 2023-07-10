package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.RankVersion;
import NotableDeveloper.rank.domain.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankVersionRepository extends JpaRepository<RankVersion, Long> {
    boolean existsByYearAndSemesterAndInjectedIsTrue(int year, Semester semester);
    boolean existsByYearAndSemesterAndClassifiedCourseIsTrue(int year, Semester semester);
    boolean existsByYearAndSemesterAndClassifiedProfessorIsTrue(int year, Semester semester);
    RankVersion findByYearAndSemester(int year, Semester semester);

    @Query("SELECT rv FROM RankVersion rv WHERE rv.year < :year OR (rv.year = :year AND rv.semester = :semester)")
    List<RankVersion> findPreviousOrSameVersions(int year, Semester semester);
}
