package NotableDeveloper.rank.test.repository;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    void setUp(){
        /*
            중복을 제외하고 강의를 DB에 등록한다.
         */
        for(Course course : data.getCourses()){
            if(courseRepository.existsByTitleAndOfferedYearAndSemesterAndCode(
                    course.getTitle(),
                    course.getOfferedYear(),
                    course.getSemester(),
                    course.getCode()
            )){
                Course updateCourse = courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                        course.getTitle(),
                        course.getOfferedYear(),
                        course.getSemester(),
                        course.getCode()
                );

                int updateCount = updateCourse.getCount() + 1;
                float updateRating = updateCourse.getRating() + course.getRating();

                updateCourse.setCount(updateCount);
                updateCourse.setRating(updateRating);

                courseRepository.save(updateCourse);
            }

            else courseRepository.save(course);
        }

        departmentRepository.saveAll(data.getDepartments());
        professorRepository.saveAll(data.getProfessors());
    }

    @Test
    @DisplayName("김철수 교수, 홍길동 강사가 각각 개설한 강의들을 등록한다.")
    void 여러교수_개설강의_정상등록_테스트() {
        List<CourseProfessor> savedCourseProfessors = courseProfessorRepository.saveAll(data.getCourseProfessors());
        List<CourseProfessor> alreadyCourseProfessors = data.getCourseProfessors();

        /*
            CourseProfessor 테이블에는 현재 세 개의 강의-교수 정보가 등록되어 있어야한다.
         */
        Assertions.assertEquals(3, courseProfessorRepository.findAll().size());

        for(int i = 0; i < savedCourseProfessors.size(); i++){
            CourseProfessor alreadyCP = alreadyCourseProfessors.get(i);
            CourseProfessor savedCP = savedCourseProfessors.get(i);

            Assertions.assertEquals(alreadyCP.getCourse().getId(), savedCP.getCourse().getId());
            Assertions.assertEquals(alreadyCP.getProfessor().getId(), savedCP.getProfessor().getId());
        }
    }
}
