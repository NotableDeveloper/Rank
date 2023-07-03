package NotableDeveloper.rank.test.service.CourseService;

import NotableDeveloper.rank.domain.dto.CourseDetailDto;
import NotableDeveloper.rank.domain.dto.ProfessorDetailDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.service.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseServiceTest {
    CourseService courseService;

    @Mock
    CourseProfessorRepository courseProfessorRepository;

    static ArrayList<CourseProfessor> courseProfessors;
    @BeforeEach
    void setUp(){
        courseProfessors = new ArrayList<>();

        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        courseService = new CourseService();
        courseService.setCourseProfessorRepository(courseProfessorRepository);

        Course oop = Course.builder()
                .title("객체지향 프로그래밍")
                .year(2023)
                .semester(Semester.FIRST)
                .code("12345678")
                .rating(0.0F)
                .build();

        oop.setId(1L);
        oop.setTier(Tier.A);

        Course sysp = Course.builder()
                .title("시스템 프로그래밍")
                .year(2023)
                .semester(Semester.FIRST)
                .code("87654321")
                .rating(0.0F)
                .build();

        sysp.setId(2L);
        sysp.setTier(Tier.A);

        Course os = Course.builder()
                .title("운영체제")
                .year(2023)
                .semester(Semester.FIRST)
                .code("24681357")
                .rating(0.0F)
                .build();

        os.setId(3L);
        os.setTier(Tier.B);

        Professor professorKim = Professor.builder()
                .name("김철수")
                .college("IT대학")
                .department(Department.builder()
                        .college("IT대학")
                        .originalName("컴퓨터학부")
                        .build())
                .position("교수")
                .build();

        professorKim.setId(1L);
        professorKim.setTier(Tier.A_MINUS);

        Professor professorPark = Professor.builder()
                .name("박철수")
                .college("IT대학")
                .department(Department.builder()
                        .college("IT대학")
                        .originalName("소프트웨어학부")
                        .build())
                .position("강사")
                .build();

        professorPark.setId(2L);
        professorPark.setTier(Tier.B_PLUS);

        courseProfessors.add(CourseProfessor.builder()
                .course(oop)
                .professor(professorKim)
                .build());

        courseProfessors.add(CourseProfessor
                .builder()
                .course(sysp)
                .professor(professorKim).build());

        courseProfessors.add(CourseProfessor
                .builder()
                .course(os)
                .professor(professorPark).build());
    }

    @Test
    @DisplayName("특정 키워드로 강의 검색을 했을 때, 결과가 없다면 예외가 발생한다.")
    void 강의명_검색_실패_테스트(){
        Mockito.when(courseProfessorRepository.findAllByCourse_TitleContains(Mockito.any()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(CourseNotFoundException.class,
                () -> courseService.getCourseByTitle("객체지향 프로그래밍"));
    }

    @Test
    @DisplayName("특정 키워드로 검색했을 때, 강의 검색이 성공적으로 수행되는 지를 검증한다.")
    void 강의명_검색_성공_테스트(){
        String title = "프로그래밍";

        /*
            사전에 준비해둔 강의-교수 정보에서 강의명이 "프로그래밍"인 것만 추출하고,
            "프로그래밍"을 검색하는 경우에 추출한 강의-교수 정보를 반환하도록 한다.
         */
        ArrayList<CourseProfessor> findByProgramming = (ArrayList<CourseProfessor>)
                courseProfessors.stream().filter(cp ->
                        cp.getCourse().getTitle().contains(title))
                        .collect(Collectors.toList());

        Mockito.when(courseProfessorRepository.findAllByCourse_TitleContains(title))
                .thenReturn(findByProgramming);

        /*
             CouserService에서 "프로그래밍"으로 강의를 검색한 경우에
             나올 것으로 예상되는 DTO List를 생성한다.
         */
        ArrayList<CourseDetailDto> exceptedCourses =
                (ArrayList<CourseDetailDto>) findByProgramming.stream()
                        .map(cp -> CourseDetailDto.builder()
                                .courseId(cp.getCourse().getId())
                                .code(cp.getCourse().getCode())
                                .year(cp.getCourse().getOfferedYear())
                                .semester(cp.getCourse().getSemester())
                                .title(cp.getCourse().getTitle())
                                .tier(cp.getCourse().getTier())
                                .professor(ProfessorDetailDto.builder()
                                        .professorId(cp.getProfessor().getId())
                                        .college(cp.getProfessor().getCollege())
                                        .department(cp.getProfessor().getDepartment().getOriginalName())
                                        .position(cp.getProfessor().getPosition())
                                        .tier(cp.getProfessor().getTier())
                                        .build())
                                .build())
                        .collect(Collectors.toList());

        List<CourseDetailDto> findCourses = courseService.getCourseByTitle(title);

        for(int i = 0; i < findCourses.size(); i++){
            CourseDetailDto findCourse = findCourses.get(i);
            CourseDetailDto expectedCourse = exceptedCourses.get(i);

            Assertions.assertEquals(expectedCourse, findCourse);
        }
    }
}
