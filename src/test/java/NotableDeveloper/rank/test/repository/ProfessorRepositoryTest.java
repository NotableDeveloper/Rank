package NotableDeveloper.rank.test.repository;

import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
public class ProfessorRepositoryTest {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    static RankData data;

    @BeforeEach
    @DisplayName("교수의 소속 학과 정보를 DB에 저장한다.")
    void setUp(){
        data = new RankData();
        departmentRepository.deleteAll();
        departmentRepository.saveAll(data.getDepartments());
    }

    @Test
    @DisplayName("한 명의 교수를 DB에 등록한다.")
    void 교수_정상등록_테스트(){
        Professor professor = data.getProfessors().get(0);
        Professor savedProfessor = professorRepository.save(professor);

        Assertions.assertEquals(professor.getId(), savedProfessor.getId());
        Assertions.assertEquals(professor.getName(), savedProfessor.getName());
        Assertions.assertEquals(professor.getCollege(), savedProfessor.getCollege());
        Assertions.assertEquals(professor.getDepartment().getOriginalName(), savedProfessor.getDepartment().getOriginalName());
        Assertions.assertEquals(professor.getPosition(), savedProfessor.getPosition());
    }
}
