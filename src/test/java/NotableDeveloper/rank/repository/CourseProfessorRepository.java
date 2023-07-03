package NotableDeveloper.rank.repository;

import NotableDeveloper.rank.domain.entity.CourseProfessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CourseProfessorRepository extends JpaRepository<CourseProfessor, Long> {
    ArrayList<CourseProfessor> findAllByProfessor_Id(Long professorId);
    boolean existsByCourse_TitleAndProfessor_Name(String title, String name);
    ArrayList<CourseProfessor> findAllByCourse_TitleContains(String title);
    CourseProfessor findByCourse_Id(Long courseId);
}
