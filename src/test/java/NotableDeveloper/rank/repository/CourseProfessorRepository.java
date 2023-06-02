package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.CourseProfessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CourseProfessorRepository extends JpaRepository<CourseProfessor, Long> {
    ArrayList<CourseProfessor> findAllByProfessor_Id(Long professorId);
}
