package NotableDeveloper.rank.test.repository;

import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@DataJpaTest
public class CourseProfessorRepositoryTest {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    CourseProfessorRepository courseProfessorRepository;

    static RankData data = new RankData();


    @BeforeEach
    @DisplayName("강의, 학과, 교수 정보를 DB에 저장한다.")
    @Transactional
    void setUp(){
        courseRepository.saveAll(data.getCourses());
        departmentRepository.saveAll(data.getDepartments());
        professorRepository.saveAll(data.getProfessors());
    }
    @Test
    @DisplayName("강의-교수 관계를 등록한다.")
    @Transactional
    void 강의_교수_등록_테스트() {
        List<CourseProfessor> savedCourseProfessors = courseProfessorRepository.saveAll(data.getCourseProfessors());
        List<CourseProfessor> alreadyCourseProfessors = data.getCourseProfessors();

        for(int i = 0; i < savedCourseProfessors.size(); i++){
            CourseProfessor alreadyCP = alreadyCourseProfessors.get(i);
            CourseProfessor savedCP = savedCourseProfessors.get(i);

            Assertions.assertEquals(alreadyCP.getCourse().getId(), savedCP.getCourse().getId());
            Assertions.assertEquals(alreadyCP.getProfessor().getId(), savedCP.getProfessor().getId());
        }
    }
}
