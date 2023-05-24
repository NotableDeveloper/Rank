package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
