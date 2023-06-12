package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByCollegeAndOriginalName(String college, String originalName);
    Department findByCollegeAndOriginalName(String college, String originalName);
}
