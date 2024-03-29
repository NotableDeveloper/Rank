package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Tier;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsByNameAndDepartment_OriginalName(String name, String originalName);
    Professor findByNameAndDepartment_OriginalName(String name, String originalName);
    List<Professor> findAllByNameContains(String name);
    List<Professor> findAllByDepartment_Id(Long departmentId);

    List<Professor> findAllByTierNot(Tier tier, Sort sort);
}
