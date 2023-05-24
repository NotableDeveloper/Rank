package NotableDeveloper.rank;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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


    @BeforeEach
    void setUp(){
        courses = new ArrayList<>();
        professors = new ArrayList<>();
        departments = new ArrayList<>();
    }
}
