package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.RankVersion;
import NotableDeveloper.rank.domain.enums.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankVersionRepository extends JpaRepository<RankVersion, Long> {
    boolean existsByYearAndSemester(int year, Semester semester);
}
