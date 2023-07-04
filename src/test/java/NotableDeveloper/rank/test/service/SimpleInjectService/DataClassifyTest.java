package NotableDeveloper.rank.test.service.SimpleInjectService;

import NotableDeveloper.rank.domain.dto.CourseDataDto;
import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.dto.ProfessorDataDto;
import NotableDeveloper.rank.domain.entity.*;
import NotableDeveloper.rank.domain.enums.Tier;
import NotableDeveloper.rank.domain.exceptiion.ClassifyAlreadyException;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationNotFoundException;
import NotableDeveloper.rank.repository.*;
import NotableDeveloper.rank.service.implement.SimpleEvaluationClassify;
import NotableDeveloper.rank.service.implement.SimpleEvaluationExtract;
import NotableDeveloper.rank.service.SimpleInjectService;
import NotableDeveloper.rank.test.data.SampleCsvExtract;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

public class DataClassifyTest {
    SimpleInjectService simpleInjectService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    ProfessorRepository professorRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    CourseProfessorRepository courseProfessorRepository;
    @Mock
    RankVersionRepository rankVersionRepository;
    ArrayList<EvaluationDto> evaluations;
    final int year = 2023;
    final Semester semester = Semester.FIRST;
    @BeforeEach
    void setUp(){
        courseRepository = Mockito.mock(CourseRepository.class);
        professorRepository = Mockito.mock(ProfessorRepository.class);
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        rankVersionRepository = Mockito.mock(RankVersionRepository.class);

        SampleCsvExtract  sampleCsv = new SampleCsvExtract();
        evaluations = sampleCsv.extractEvaluation();
        SimpleEvaluationExtract extract = new SimpleEvaluationExtract();
        extract.setEvaluations(evaluations);
        extract.extractEvaluation();

        SimpleEvaluationClassify simpleEvaluationClassify = new SimpleEvaluationClassify();

        simpleInjectService = new SimpleInjectService();
        simpleInjectService.setCourseRepository(courseRepository);
        simpleInjectService.setProfessorRepository(professorRepository);
        simpleInjectService.setCourseProfessorRepository(courseProfessorRepository);
        simpleInjectService.setDepartmentRepository(departmentRepository);
        simpleInjectService.setRankVersionRepository(rankVersionRepository);
        simpleInjectService.setExtractor(extract);
        simpleInjectService.setClassification(simpleEvaluationClassify);

        /*
            메서드 테스트 과정에서 무조건 rankVersionRepository의 existsByYearAndSemesterAndInjectedIsTrue 메서드와
            existsByYearAndSemesterAndCalculatedIsTrue 메서드, save 메서드가 호출되므로 이에 대한 설정을 해둔다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
                .thenReturn(true)
                .thenReturn(false);

        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndClassifiedCourseIsTrue(year, semester))
                .thenReturn(false)
                .thenReturn(true);

        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndClassifiedProfessorIsTrue(year, semester))
                .thenReturn(false)
                .thenReturn(true);

        Mockito.when(rankVersionRepository.findByYearAndSemester(year, semester))
                .thenReturn(RankVersion.builder()
                        .year(year)
                        .semester(semester)
                        .build());

        Mockito.when(rankVersionRepository.save(Mockito.any()))
                .thenReturn(RankVersion.builder()
                        .year(year)
                        .semester(semester)
                        .build());
    }
    @Test
    @DisplayName("등급을 부여하기 이전에 강의 정보가 주입되어 있지 않으면 예외가 발생한다.")
    void 강의정보_주입되지_않은경우_예외발생_테스트(){
        /*
            updateCourses, updateProfessors를 호출할 때, 사전에 강의 정보가 주입되어 있지 않으면
            예외가 발생한다.
         */
        Assertions.assertDoesNotThrow(() ->
                simpleInjectService.updateCourses(year, semester));

        Assertions.assertThrows(EvaluationNotFoundException.class,
                () -> simpleInjectService.updateCourses(year, semester));
    }

    @Test
    @DisplayName("강의 데이터에 등급을 부여하는 행동을 중복으로 수행하면 예외가 발생한다.")
    void 강의_등급_중복방지_테스트(){
        /*
            updateCourses 메서드가 같은 학기에 두 번 호출 되었고, 첫 번째 호출에서
            성공적으로 강의평가 데이터를 주입하였다면 두 번째 호출에서는 예외가 발생해야 한다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
                .thenReturn(true);

        Assertions.assertDoesNotThrow(() ->
                simpleInjectService.updateCourses(year, semester));

        Assertions.assertThrows(ClassifyAlreadyException.class,
                () -> simpleInjectService.updateCourses(year, semester));
    }

    @Test
    @DisplayName("교수 데이터에 등급을 부여하는 행동을 중복으로 수행하면 예외가 발생한다.")
    void 교수_등급_중복방지_테스트(){
        /*
            updateProfessors 메서드가 같은 학기에 두 번 호출 되었고, 첫 번째 호출에서
            성공적으로 강의평가 데이터를 주입하였다면 두 번째 호출에서는 예외가 발생해야 한다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
                .thenReturn(true);

        Assertions.assertDoesNotThrow(() ->
                simpleInjectService.updateProfessors(year, semester));

        Assertions.assertThrows(ClassifyAlreadyException.class,
                () -> simpleInjectService.updateProfessors(year, semester));
    }

    @Test
    @DisplayName("강의 데이터에 등급을 부여할 때, 백분율을 계산한다.")
    void 강의_등급_정상부여_테스트(){
        /*
            강의 데이터에 등급을 부여할 때, 저장될 것으로 예상되는 데이터들을 준비한다.
            expectedCourses는 중복되지 않는(= 분반을 고려한) 강의들을 저장한 List이고,
            courses는 중복을 포함하는(= 분반이 분반을 고려하지 않은) 강의들을 저장한 List이다.
         */
        ArrayList<CourseDataDto> expectedCourses = new ArrayList<>();
        Map<List<Object>, CourseDataDto> keyMap = new HashMap<>();
        List<CourseDataDto> courses = simpleInjectService.getExtractor().getCourses();

        courses.forEach(course -> {
            List<Object> key = Arrays.asList(course.getTitle(),
                    course.getYear(),
                    course.getCode(),
                    course.getSemester()
                    );

            if(keyMap.containsKey(key)){
                CourseDataDto existingCourse = keyMap.get(key);
                existingCourse.setCount(existingCourse.getCount() + 1);
                existingCourse.setRating(existingCourse.getRating() + course.getRating());
            }

            else{
                keyMap.put(key, course);
                expectedCourses.add(course);
            }
        });

        expectedCourses.forEach(course -> course.setAverage(course.getRating() / course.getCount()));
        Collections.sort(expectedCourses);

        /*
            expectedCourses에 등급을 부여한다.
         */
        int totalCourses = expectedCourses.size();

        for (int i = 0; i < totalCourses; i++) {
            float percentage = (float)(i + 1) / totalCourses * 100;

            if(0.0F <= percentage && percentage <= 30.00F)
                expectedCourses.get(i).setTier(Tier.A);

            else if(30.00F < percentage && percentage <= 70.00F)
                expectedCourses.get(i).setTier(Tier.B);

            else if(70.00F < percentage && percentage <= 85.00F)
                expectedCourses.get(i).setTier(Tier.C);

            else
                expectedCourses.get(i).setTier(Tier.D);
        }

        /*
            Mock 객체인 courseRepository 강의를 검색하는 메서드에 대한 처리를 한다.
         */
        List<Course> findCourses = courses.stream()
                .map(courseDto ->
                        new Course(courseDto.getTitle(),
                                courseDto.getYear(),
                                courseDto.getSemester(),
                                courseDto.getCode(),
                                courseDto.getRating())
                ).collect(Collectors.toList());

        Mockito.when(courseRepository.findAllPreviousOrSameVersions(year, semester))
                        .thenReturn(findCourses);

        for(CourseDataDto expectedCourse : expectedCourses){
            Course findCourse = new Course(
                    expectedCourse.getTitle(),
                    expectedCourse.getYear(),
                    expectedCourse.getSemester(),
                    expectedCourse.getCode(),
                    expectedCourse.getRating()
            );

            Mockito.when(courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                    expectedCourse.getTitle(),
                    expectedCourse.getYear(),
                    expectedCourse.getSemester(),
                    expectedCourse.getCode()
            )).thenReturn(findCourse);
        }

        simpleInjectService.updateCourses(year, semester);

        /*
            expectedCourses와 DB에 저장될 savedCourses를 비교하여 검증한다.
            또, DB 저장이 expectedCourses의 size만큼 호출되는 지를 검증한다.
         */
        List<CourseDataDto> savedCourses = simpleInjectService.getClassification().getUniqueCourses();

        for(CourseDataDto expected : expectedCourses){
            Assertions.assertEquals(true, savedCourses.contains(expected));
        }

        Mockito.verify(courseRepository,
                Mockito.times(expectedCourses.size()))
                .save(Mockito.any());
    }

    @Test
    @DisplayName("교수 데이터에 등급을 부여할 때, 백분율을 계산한다.")
    void 교수_등급_정상부여_테스트(){
        ArrayList<ProfessorDataDto> expectedProfessors = new ArrayList<>();
        Map<List<Object>, ProfessorDataDto> keyMap = new HashMap<>();
        List<ProfessorDataDto> professors = simpleInjectService.getExtractor().getProfessors();

        professors.forEach(professor -> {
            List<Object> key = Arrays.asList(
                    professor.getName(),
                    professor.getCollege(),
                    professor.getDepartment(),
                    professor.getPosition());

            if(!keyMap.containsKey(key)){
                keyMap.put(key, professor);
                expectedProfessors.add(professor);
            }
        });

        expectedProfessors.forEach(professor -> professor.setAverage(professor.getRating() / professor.getCount()));
        Collections.sort(expectedProfessors);

        int totalProfessors = expectedProfessors.size();

        for(int i = 0; i < totalProfessors; i++){
            float percentage = (float)(i + 1) / totalProfessors * 100;

            if(0.0F < percentage && percentage < 10.00F)
                expectedProfessors.get(i).setTier(Tier.A_PLUS);

            else if(10.00F <= percentage && percentage < 20.00F)
                expectedProfessors.get(i).setTier(Tier.A);

            else if(20.00F <= percentage && percentage < 30.00F)
                expectedProfessors.get(i).setTier(Tier.A_MINUS);

            else if(30.0F <= percentage && percentage < 40.00F)
                expectedProfessors.get(i).setTier(Tier.B_PLUS);

            else if(40.00F <= percentage && percentage < 60.00F)
                expectedProfessors.get(i).setTier(Tier.B);

            else if(60.00F <= percentage && percentage < 70.00F)
                expectedProfessors.get(i).setTier(Tier.B_MINUS);

            else if(70.00F <= percentage && percentage < 75.00F)
                expectedProfessors.get(i).setTier(Tier.C_PLUS);

            else if(75.00F <= percentage && percentage < 80.00F)
                expectedProfessors.get(i).setTier(Tier.C);

            else if(80.00F <= percentage && percentage < 85.00F)
                expectedProfessors.get(i).setTier(Tier.C_MINUS);

            else if(85.00F <= percentage && percentage < 90.00F)
                expectedProfessors.get(i).setTier(Tier.D_PLUS);

            else if(90.00F <= percentage && percentage < 95.00F)
                expectedProfessors.get(i).setTier(Tier.D);

            else
                expectedProfessors.get(i).setTier(Tier.D_MINUS);
        }

        for(ProfessorDataDto professor : expectedProfessors) {
            Mockito.when(professorRepository.findByNameAndDepartment_OriginalName(
                    professor.getName(), professor.getDepartment()))
                    .thenReturn(Professor.builder()
                            .name(professor.getName())
                            .college(professor.getCollege())
                            .position(professor.getPosition())
                            .department(Department.builder()
                                    .college(professor.getCollege())
                                    .originalName(professor.getDepartment())
                                    .build())
                            .build());
        }

        simpleInjectService.getClassification().setUniqueProfessors(expectedProfessors);
        simpleInjectService.updateProfessors(year, semester);
        List<ProfessorDataDto> savedProfessors = simpleInjectService.getClassification().getUniqueProfessors();

        for(ProfessorDataDto expected : expectedProfessors)
            Assertions.assertEquals(true, savedProfessors.contains(expected));

        Mockito.verify(professorRepository,
                Mockito.times(expectedProfessors.size()))
                .save(Mockito.any());
    }
}
