package NotableDeveloper.rank;

import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;


@DataJpaTest
public class ProfessorRepositoryTest {
    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    static ArrayList<Professor> professors;
    static ArrayList<Department> departments;

    @BeforeEach
    @DisplayName("컴퓨터학부, 소프트웨어학부를 DB에 저장하고, 각 학부에 소속될 교수를 준비한다.")
    void setUp(){
        departments = new ArrayList<>();
        professors = new ArrayList<>();

        Department computer = new Department("IT대학", "컴퓨터학부", "컴퓨터");
        Department soft = new Department("IT대학", "소프트웨어학부", "소프트");

        departments.add(computer);
        departments.add(soft);
        departmentRepository.saveAll(departments);

        professors.add(new Professor("김철수", "IT대학", computer, "교수"));
        professors.add(new Professor("홍길동", "IT대학", soft, "강사"));
    }

    @Test
    @DisplayName("IT대학 컴퓨터학부 소속 김철수 교수를 DB에 등록한다.")
    void 교수_정상등록_테스트(){
        Professor professorKim = professors.get(0);
        Professor savedProfessor = professorRepository.save(professorKim);

        Assertions.assertEquals("김철수", savedProfessor.getName());
        Assertions.assertEquals("IT대학", savedProfessor.getCollege());
        Assertions.assertEquals("컴퓨터학부", savedProfessor.getDepartment().getOriginalName());
        Assertions.assertEquals("교수", savedProfessor.getPosition());
    }
}
