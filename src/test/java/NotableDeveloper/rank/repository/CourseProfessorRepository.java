package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.CourseProfessor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface CourseProfessorRepository extends JpaRepository<CourseProfessor, Long> {
    ArrayList<CourseProfessor> findAllByProfessor_Id(Long professorId);
}
