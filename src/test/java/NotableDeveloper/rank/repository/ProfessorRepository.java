package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
