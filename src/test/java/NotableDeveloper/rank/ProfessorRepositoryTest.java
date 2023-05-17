package NotableDeveloper.rank;

import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("컴퓨터학부, 소프트웨어학부와 각 학부에 소속될 교수를 준비한다.")
    void setUp(){
        departments = new ArrayList<>();

        Department computer = new Department("IT대학", "컴퓨터학부", "컴퓨터");
        Department soft = new Department("IT대학", "소프트웨어학부", "소프트");

        departments.add(computer);
        departments.add(soft);

        professors.add(new Professor("김철수", "IT대학", computer, "교수"));
        professors.add(new Professor("홍길동", "IT대학", soft, "강사"));
    }
}
