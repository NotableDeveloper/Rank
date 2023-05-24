package NotableDeveloper.rank;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;

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

    static ArrayList<Course> courses;
    static ArrayList<Professor> professors;
    static ArrayList<Department> departments;
    static ArrayList<CourseProfessor> courseProfessors;

    @BeforeEach
    void setUp(){
        courses = new ArrayList<>();
        professors = new ArrayList<>();
        departments = new ArrayList<>();
        courseProfessors = new ArrayList<>();

        // 강의 정보 저장
        courses.add(new Course("객체지향 프로그래밍", 2021, Semester.FIRST, "50341233", 79.00F));
        courses.add(new Course("시스템 프로그래밍", 2022, Semester.FIRST, "50301870", 76.00F));
        courses.add(new Course("데이터베이스", 2023, Semester.FIRST, "50321144", 82.00F));

        // 강의 DB 등록
        courseRepository.saveAll(courses);

        // 학과 정보 저장
        Department computer = new Department("IT대학", "컴퓨터학부", "컴퓨터");
        Department soft = new Department("IT대학", "소프트웨어학부", "소프트");

        departments.add(computer);
        departments.add(soft);

        // 학과 정보 DB 등록
        departmentRepository.saveAll(departments);

        // 교수 정보 저장
        professors.add(new Professor("김철수", "IT대학", computer, "교수"));
        professors.add(new Professor("홍길동", "IT대학", soft, "강사"));

        // 교수 DB 등록
        professorRepository.saveAll(professors);

        /*
            2021년 1학기 객체지향 프로그래밍 강의와 2022년 1학기 시스템 프로그래밍 강의는
            컴퓨터학부의 김철수 교수가 담당하였다.
            2023년 1학기 데이터베이스 강의는 소프트웨어학부의 홍길동 강사가 담당하였다.
         */
        courseProfessors.add(new CourseProfessor(courses.get(0), professors.get(0)));
        courseProfessors.add(new CourseProfessor(courses.get(1), professors.get(0)));
        courseProfessors.add(new CourseProfessor(courses.get(2), professors.get(1)));
    }
}
