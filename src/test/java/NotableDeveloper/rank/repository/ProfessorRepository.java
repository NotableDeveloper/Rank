package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsByNameAndDepartment_OriginalName(String name, String originalName);
    Professor findByNameAndDepartment_OriginalName(String name, String originalName);
}
