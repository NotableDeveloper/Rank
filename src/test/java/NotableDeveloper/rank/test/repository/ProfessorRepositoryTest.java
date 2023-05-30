package NotableDeveloper.rank.test.repository;

import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
public class ProfessorRepositoryTest {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    static RankData data = new RankData();

    @BeforeEach
    @DisplayName("교수의 소속 학과 정보를 DB에 저장한다.")
    void saveDepartment(){
        departmentRepository.saveAll(data.getDepartments());
    }

    @Test
    @DisplayName("IT대학 컴퓨터학부 소속 김철수 교수를 DB에 등록한다.")
    void 교수_정상등록_테스트(){
        Professor professorKim = data.getProfessors().get(0);
        Professor savedProfessor = professorRepository.save(professorKim);

        Assertions.assertEquals("김철수", savedProfessor.getName());
        Assertions.assertEquals("IT대학", savedProfessor.getCollege());
        Assertions.assertEquals("컴퓨터학부", savedProfessor.getDepartment().getOriginalName());
        Assertions.assertEquals("교수", savedProfessor.getPosition());
    }

    @Test
    @DisplayName("준비된 모든 교수를 DB에 등록한다.")
    void 여러교수_정상등록_테스트(){
        professorRepository.saveAll(data.getProfessors());
        List<Professor> savedProfessors = professorRepository.findAll();

        for(int i = 0; i < savedProfessors.size(); i++){
            Professor alreadyProfessor = data.getProfessors().get(i);
            Professor savedProfessor = savedProfessors.get(i);

            Assertions.assertEquals(alreadyProfessor.getName(), savedProfessor.getName());
            Assertions.assertEquals(alreadyProfessor.getCollege(), savedProfessor.getCollege());
            Assertions.assertEquals(alreadyProfessor.getDepartment(), savedProfessor.getDepartment());
            Assertions.assertEquals(alreadyProfessor.getPosition(), savedProfessor.getPosition());
        }
    }
}
