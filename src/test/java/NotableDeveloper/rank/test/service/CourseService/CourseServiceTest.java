package NotableDeveloper.rank.test.service.CourseService;

import NotableDeveloper.rank.domain.dto.CourseDetailDto;
import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.CourseHistoryDto;
import NotableDeveloper.rank.domain.dto.ProfessorDetailDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.service.CourseService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseServiceTest {
    CourseService courseService;

    @Mock
    CourseProfessorRepository courseProfessorRepository;

    @Mock
    CourseRepository courseRepository;
    static ArrayList<CourseProfessor> courseProfessors;
    @BeforeEach
    void setUp(){
        courseProfessors = new ArrayList<>();

        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        courseRepository = Mockito.mock(CourseRepository.class);

        courseService = new CourseService();
        courseService.setCourseProfessorRepository(courseProfessorRepository);
        courseService.setCourseRepository(courseRepository);

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

        Course os1 = Course.builder()
                .title("운영체제")
                .year(2023)
                .semester(Semester.FIRST)
                .code("24681357")
                .rating(0.0F)
                .build();

        os1.setId(3L);
        os1.setTier(Tier.B);

        Course os2 = Course.builder()
                .title("운영체제")
                .year(2024)
                .semester(Semester.FIRST)
                .code("24681357")
                .rating(0.0F)
                .build();

        os2.setId(4L);
        os2.setTier(Tier.A);

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

        courseProfessors.add(CourseProfessor.builder()
                .course(sysp)
                .professor(professorKim)
                .build());

        courseProfessors.add(CourseProfessor.builder()
                .course(os1)
                .professor(professorPark)
                .build());

        courseProfessors.add(CourseProfessor.builder()
                .course(os2)
                .professor(professorPark)
                .build());
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
        ArrayList<CourseProfessor> findAllByProgramming = (ArrayList<CourseProfessor>)
                courseProfessors.stream().filter(cp ->
                        cp.getCourse().getTitle().contains(title))
                        .collect(Collectors.toList());

        Mockito.when(courseProfessorRepository.findAllByCourse_TitleContains(title))
                .thenReturn(findAllByProgramming);

        /*
             CouserService에서 "프로그래밍"으로 강의를 검색한 경우에
             나올 것으로 예상되는 DTO List를 생성한다.
         */
        ArrayList<CourseDto> exceptedCourses =
                (ArrayList<CourseDto>) findAllByProgramming.stream()
                        .map(cp -> CourseDto.builder()
                                .courseId(cp.getCourse().getId())
                                .title(cp.getCourse().getTitle())
                                .year(cp.getCourse().getOfferedYear())
                                .semester(cp.getCourse().getSemester())
                                .tier(cp.getCourse().getTier())
                                .professor(cp.getProfessor().getName())
                                .department(cp.getProfessor().getDepartment().getOriginalName())
                                .build())
                        .collect(Collectors.toList());

        List<CourseDto> findCourses = courseService.getCourseByTitle(title);

        for(int i = 0; i < findCourses.size(); i++){
            CourseDto findCourse = findCourses.get(i);
            CourseDto expectedCourse = exceptedCourses.get(i);

            Assertions.assertEquals(expectedCourse.getCourseId(), findCourse.getCourseId());
        }
    }

    @Test
    @DisplayName("강의 ID로 검색할 때, 해당하는 강의가 없으면 예외를 반환한다.")
    void 강의ID_검색_실패_테스트(){
        Mockito.when(courseProfessorRepository.findByCourse_Id(Mockito.any()))
                .thenReturn(null);

        Assertions.assertThrows(CourseNotFoundException.class,
                () -> courseService.getCourseById(1L));
    }

    @Test
    @DisplayName("강의 ID로 검색할 때, 해당하는 강의가 있는 지와 강의 내역을 잘 가져오는 지를 검증한다.")
    void 강의ID_검색_성공_테스트(){
        Long courseId = 3L;

        CourseProfessor findByCourseId = courseProfessors.stream()
                .filter(cp -> cp.getCourse().getId().equals(courseId))
                .findFirst()
                .orElse(null);

        Course c = findByCourseId.getCourse();
        Professor p = findByCourseId.getProfessor();

        List<Course> findAllByCourseCode = courseProfessors.stream()
                .filter(cp -> cp.getCourse().getCode() == c.getCode())
                .map(cp -> {
                    Course course = cp.getCourse();
                    return course;
                }).collect(Collectors.toList());

        List<CourseHistoryDto> history = findAllByCourseCode.stream()
                .map(course -> CourseHistoryDto.builder()
                        .courseId(course.getId())
                        .tier(course.getTier())
                        .year(course.getOfferedYear())
                        .semester(course.getSemester())
                        .build()
                ).collect(Collectors.toList());

        ProfessorDetailDto professorDetailDto = ProfessorDetailDto.builder()
                .professorId(p.getId())
                .name(p.getName())
                .college(p.getCollege())
                .department(p.getDepartment().getOriginalName())
                .position(p.getPosition())
                .tier(p.getTier())
                .build();

        CourseDetailDto exceptedCourse = CourseDetailDto.builder()
                .courseId(c.getId())
                .code(c.getCode())
                .semester(c.getSemester())
                .year(c.getOfferedYear())
                .courseTier(c.getTier())
                .professor(professorDetailDto)
                .history(history)
                .build();

        Mockito.when(courseProfessorRepository.findByCourse_Id(courseId))
                .thenReturn(findByCourseId);

        Mockito.when(courseRepository.findAllByCode(c.getCode()))
                .thenReturn(findAllByCourseCode);

        CourseDetailDto findCourse = courseService.getCourseById(courseId);

        Assertions.assertEquals(exceptedCourse.getCourseId(), findCourse.getCourseId());

        for(int i = 0; i < findCourse.getHistory().size(); i++){
            CourseHistoryDto saved = findCourse.getHistory().get(i);
            CourseHistoryDto excepted = history.get(i);

            Assertions.assertEquals(excepted.getYear(), saved.getYear());
            Assertions.assertEquals(excepted.getSemester(), saved.getSemester());
            Assertions.assertEquals(excepted.getTier(), saved.getTier());
        }
    }
}
