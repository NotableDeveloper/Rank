package NotableDeveloper.rank.test.service.CourseService;

import NotableDeveloper.rank.domain.dto.*;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.service.CourseService;
import NotableDeveloper.rank.test.data.RankData;
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
    static RankData rankData = new RankData();
    @BeforeEach
    void setUp(){
        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        courseRepository = Mockito.mock(CourseRepository.class);

        courseService = new CourseService();
        courseService.setCourseProfessorRepository(courseProfessorRepository);
        courseService.setCourseRepository(courseRepository);
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
                rankData.getCourseProfessors().stream().filter(cp ->
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

        CourseProfessor findByCourseId = rankData.getCourseProfessors().stream()
                .filter(cp -> cp.getCourse().getId().equals(courseId))
                .findFirst()
                .orElse(null);

        Course c = findByCourseId.getCourse();
        Professor p = findByCourseId.getProfessor();

        List<Course> findAllByCourseCode = rankData.getCourseProfessors().stream()
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

        ProfessorDto professorDto = ProfessorDto.builder()
                .professorId(p.getId())
                .name(p.getName())
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
                .professor(professorDto)
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
