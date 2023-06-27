package NotableDeveloper.rank.test.service.SimpleInjectService;

import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.DepartmentDto;
import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.entity.RankVersion;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.repository.*;
import NotableDeveloper.rank.service.implement.SimpleEvaluationExtract;
import NotableDeveloper.rank.service.SimpleInjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;


public class DataInjectTest {
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
    @DisplayName("Mock 객체와 강의 평가 데이터를 준비한다.")
    void setUp(){
        courseRepository = Mockito.mock(CourseRepository.class);
        professorRepository = Mockito.mock(ProfessorRepository.class);
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        rankVersionRepository = Mockito.mock(RankVersionRepository.class);

        evaluations = new ArrayList<>();
        String[] code = {"12345678", "87654321", "24681357", "13572468", "13572468", "13572468"};
        String[] title = {"데이터베이스", "인공지능", "컴퓨터그래픽", "운영체제", "운영체제", "운영체제"};
        String[] professor = {"홍길동", "김철수", "박철수", "홍길동", "홍길동", "홍길동"};
        String[] department = {"컴퓨터학부", "소프트웨어학부", "글로벌미디어학부", "컴퓨터학부", "컴퓨터학부", "컴퓨터학부"};
        float[] rating = {80.03F, 94.02F, 97.00F, 86.32F, 83.32F, 88.32F};

        for(int i = 0; i < code.length; i++){
            evaluations.add(
                    EvaluationDto.builder()
                            .year(2023)
                            .semester(Semester.FIRST)
                            .code(code[i])
                            .title(title[i])
                            .professorName(professor[i])
                            .college("IT대학")
                            .department(department[i])
                            .position("교수")
                            .rating(rating[i])
                            .build()
            );
        }

        /*
            SimpleInjectService를 생성하고, Setter 주입으로 Mock 객체들을 넣어준다.
         */
        SimpleEvaluationExtract extract = new SimpleEvaluationExtract();
        extract.setEvaluations(evaluations);

        simpleInjectService = new SimpleInjectService();
        simpleInjectService.setCourseRepository(courseRepository);
        simpleInjectService.setProfessorRepository(professorRepository);
        simpleInjectService.setCourseProfessorRepository(courseProfessorRepository);
        simpleInjectService.setDepartmentRepository(departmentRepository);
        simpleInjectService.setRankVersionRepository(rankVersionRepository);
        simpleInjectService.setExtractor(extract);

        /*
            메서드 테스트 과정에서 무조건 rankVersionRepository의 existsByYearAndSemesterAndInjectedIsTrue 메서드와
            save 메서드가 호출되므로, 이에 대한 설정을 해둔다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemesterAndInjectedIsTrue(year, semester))
                .thenReturn(false)
                .thenReturn(true);

        Mockito.when(rankVersionRepository.save(Mockito.any()))
                .thenReturn(new RankVersion(year, semester));
    }

    @Test
    @DisplayName("강의 평가 데이터 주입은 오직 한번만 수행되어야 한다.")
    void 데이터_중복저장_방지_테스트() {
        /*
            saveEvaluates()가 같은 학기에 두 번 호출 되었고, 첫 번째 호출에서
            성공적으로 강의평가 데이터를 주입하였다면 두 번째 호출에서는 예외가 발생해야 한다.
         */
        Assertions.assertDoesNotThrow(() ->
                simpleInjectService.saveEvaluates(year, semester));

        Assertions.assertThrows(EvaluationAlreadyException.class,
                () -> simpleInjectService.saveEvaluates(year, semester));

    }

    @Test
    @DisplayName("강의 평가 데이터가 주입되는 과정에서 학과 정보가 주입된다.")
    void 학과정보_주입_테스트(){
        /*
            강의 평가 데이터가 주입되는 과정에서 학과 정보는 중복 되지 않고 한 번씩만
            DB에 등록 되어야 한다.
         */
        HashSet<DepartmentDto> savedDepartments;

        savedDepartments = evaluations.stream().map(evaluations ->
                        new DepartmentDto(evaluations.getCollege(), evaluations.getDepartment()))
                .collect(Collectors.toCollection(HashSet::new));

        simpleInjectService.saveEvaluates(2023, Semester.FIRST);

        for(DepartmentDto savedDepartment : savedDepartments) {
            String college = savedDepartment.getCollege();
            String originalName = savedDepartment.getOriginalName();

            Mockito.verify(departmentRepository,
                    Mockito.times(1))
                    .existsByCollegeAndOriginalName(college, originalName);
        }

        Mockito.verify(departmentRepository,
                Mockito.times(savedDepartments.size()))
                .save(Mockito.any(Department.class));
    }

    @Test
    @DisplayName("강의 평가 데이터가 주입되는 과정에서 강의 정보가 주입된다.")
    void 강의정보_주입_테스트() {
        /*
            강의 평가 데이터가 주입되는 과정에서 강의 정보는 중복 되는 경우에 기존의 강의 정보
            Entity에서 count, rating 값이 갱신 되도록 하여 DB에 등록 되어야 한다.
         */

        // evaluationCourses : 중복이 포함된(= 여러 분반이 포함딘) 강의 정보 List이다.
        ArrayList<CourseDto> evaluationCourses =
                (ArrayList<CourseDto>) evaluations.stream().map(evaluations ->
                        new CourseDto(evaluations.getTitle(),
                                evaluations.getYear(),
                                evaluations.getSemester(),
                                evaluations.getCode(),
                                evaluations.getRating()
                        )).collect(Collectors.toList());

        // uniqueCourses : 중복을 포함하지 않는(= 여러 분반을 하나로 합친) 강의 정보이며, 실제 DB에 등록되는 형태의 Map이다.
        Map<String, CourseDto> uniqueCourses = new HashMap<>();

        for (CourseDto courseDto : evaluationCourses) {
            if (!uniqueCourses.containsKey(courseDto.getCode())) uniqueCourses.put(courseDto.getCode(), courseDto);

            else {
                CourseDto existingDto = uniqueCourses.get(courseDto.getCode());
                existingDto.setCount(existingDto.getCount() + 1);
                existingDto.setRating(existingDto.getRating() + courseDto.getRating());
            }
        }

        /*
            여러 분반으로 개설된 강의를 메서드로 조회할 때, 처음 호출 시에는 등록되지 않은 것으로 동작하게
            하고 이후 메소드 호출 시에는 등록된 것처럼 처리한다.

            이미 등록된 강의를 찾아 갱신하려 할 때, 임의의 Course 객체를 만들고 그 값을 등록 하도록 처리한다.
         */
        for(CourseDto courseDto : uniqueCourses.values()) {
            Mockito.when(courseRepository.existsByTitleAndOfferedYearAndSemesterAndCode(
                            courseDto.getTitle(),
                            courseDto.getYear(),
                            courseDto.getSemester(),
                            courseDto.getCode()
                    ))
                    .thenReturn(false)
                    .thenReturn(true);

            Course findCourse = new Course(
                    courseDto.getTitle(),
                    courseDto.getYear(),
                    courseDto.getSemester(),
                    courseDto.getCode(),
                    courseDto.getRating());

            findCourse.setCount(courseDto.getCount());

            Mockito.when(courseRepository.findByTitleAndOfferedYearAndSemesterAndCode(
                    courseDto.getTitle(),
                    courseDto.getYear(),
                    courseDto.getSemester(),
                    courseDto.getCode()
            )).thenReturn(findCourse);
        }

        simpleInjectService.saveEvaluates(2023, Semester.FIRST);

        /*
            이미 등록되었는 지를 조회하는 메서드가 Count 값만큼 호출되는 지와 갱신 시에 이미 등록된 강의 정보를
            찾는 메서드가 Count 값 이상으로 호출되는 지를 검증한다.
         */
        Mockito.verify(courseRepository,
                        Mockito.times(evaluationCourses.size()))
                .save(Mockito.any(Course.class));

        for(CourseDto courseDto : evaluationCourses) {
            int count = uniqueCourses.get(courseDto.getCode()).getCount();

            Mockito.verify(courseRepository,
                            Mockito.times(count))
                    .existsByTitleAndOfferedYearAndSemesterAndCode(
                            courseDto.getTitle(),
                            courseDto.getYear(),
                            courseDto.getSemester(),
                            courseDto.getCode());
        }

        for(CourseDto courseDto : uniqueCourses.values()){
            if(courseDto.getCount() > 1){
                Mockito.verify(courseRepository,
                        Mockito.atLeast(courseDto.getCount()))
                        .findByTitleAndOfferedYearAndSemesterAndCode(
                                courseDto.getTitle(),
                                courseDto.getYear(),
                                courseDto.getSemester(),
                                courseDto.getCode());
            }
        }
    }

    @Test
    @DisplayName("강의 평가 데이터가 주입되는 과정에서 교수 정보가 주입된다.")
    void 교수정보_주입_테스트(){
        /*
            강의 평가 데이터가 주입되는 과정에서 교수 정보는 중복되지 않고 저장되어야 한다.
            또, 사전에 학과 정보가 먼저 저장되어 있어야 한다.
         */
        ArrayList<ProfessorDto> evaluationProfessors =
                (ArrayList<ProfessorDto>) evaluations.stream().map(evaluations ->
                        new ProfessorDto(evaluations.getProfessorName(),
                                evaluations.getCollege(),
                                evaluations.getDepartment(),
                                evaluations.getPosition()
                        )).collect(Collectors.toList());

        long mockId = 1;
        /*
            학과 정보를 검색하는 메서드에 대해 처리한다.
            또, 이미 등록된 교수인 지를 확인하는 메서드에 대해서도 처리한다.
         */
        for(ProfessorDto professorDto : evaluationProfessors){
            Department mockDepartment = new Department(
                    professorDto.getCollege(),
                    professorDto.getDepartment());

            mockDepartment.setId(mockId++);

            Mockito.when(departmentRepository.findByCollegeAndOriginalName(
                    professorDto.getCollege(),
                    professorDto.getDepartment()))
                    .thenReturn(mockDepartment);

            Mockito.when(professorRepository.existsByNameAndDepartment_OriginalName(
                            professorDto.getName(),
                            professorDto.getDepartment()
                            ))
                    .thenReturn(false)
                    .thenReturn(true);
        }


        simpleInjectService.saveEvaluates(2023, Semester.FIRST);

        /*
            이미 등록된 교수인 지를 확인하는 메서드는 강의 평가의 데이터 수 만큼 호출되는 지를 검증한다.
            또, 교수를 등록하는 과정에서 호출되는 메서드가 강의 평가 데이터 내에서 중복을 제외한 교수의 수 만큼
            호출 되는 지를 검증한다.
         */
        Mockito.verify(professorRepository,
                Mockito.times(evaluationProfessors.size())
                ).existsByNameAndDepartment_OriginalName(Mockito.any(), Mockito.any());

        int offset = (int) evaluationProfessors.stream().distinct().count();

        Mockito.verify(departmentRepository,
                Mockito.times(offset)
        ).findByCollegeAndOriginalName(Mockito.any(), Mockito.any());

        Mockito.verify(professorRepository,
                        Mockito.times(offset))
                .save(Mockito.any(Professor.class));
    }

    @Test
    @DisplayName("강의 평가 데이터가 주입되는 과정에서 강의-교수 정보가 주입된다.")
    void 강의교수정보_주입_테스트(){
        /*
            강의 평가 데이터가 주입되는 과정에서 강의-교수 정보는 중복되지 않고 저장되어야 한다.
         */

        HashMap<String, String> courseProfessors = new HashMap<>();

        evaluations.forEach(evaluation -> {
            courseProfessors.put(evaluation.getTitle(), evaluation.getProfessorName());
        });

        for(EvaluationDto evaluation : evaluations) {
            Mockito.when(courseProfessorRepository.existsByCourse_TitleAndProfessor_Name(
                            evaluation.getTitle(),
                            evaluation.getProfessorName()))
                    .thenReturn(false)
                    .thenReturn(true);
        }

        simpleInjectService.saveEvaluates(2023, Semester.FIRST);

        /*
            강의-교수 정보가 이미 등록되었는 지를 조회하는 메서드가 강의 평가 데이터 수 만큼 호출 되는 지를 검증 한다.
            강의-교수 정보를 저장하는 메서드가 강의 평가 데이터에서 중복을 제외한 수 만큼 호출 되는 지를 검증 한다.
            강의-교수 정보를 등록하는 과정에서 호출되는 메서드가 알맞게 호출되는 지를 검증한다.
         */
        Mockito.verify(courseProfessorRepository,
                        Mockito.times(evaluations.size()))
                .existsByCourse_TitleAndProfessor_Name(
                        Mockito.any(),
                        Mockito.any());

        Mockito.verify(courseProfessorRepository,
                Mockito.times(courseProfessors.size()))
                .save(Mockito.any());

        for(EvaluationDto evaluation : evaluations){
            Mockito.verify(courseRepository,
                    Mockito.times(1))
                    .findByTitleAndOfferedYearAndSemesterAndCode(
                            evaluation.getTitle(),
                            evaluation.getYear(),
                            evaluation.getSemester(),
                            evaluation.getCode().substring(0, 8)
                    );

            Mockito.verify(professorRepository,
                    Mockito.atLeastOnce())
                    .findByNameAndDepartment_OriginalName(
                            evaluation.getProfessorName(),
                            evaluation.getDepartment()
                    );
        }
    }
}
