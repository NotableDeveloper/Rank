package NotableDeveloper.rank.test.service;

import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.DepartmentDto;
import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.repository.*;
import NotableDeveloper.rank.service.SimpleEvaluationExtract;
import NotableDeveloper.rank.service.SimpleInjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.*;
import java.util.stream.Collectors;


public class DataInjectServiceTest {
    @Mock
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

    @BeforeEach
    void setUp(){
        courseRepository = Mockito.mock(CourseRepository.class);
        professorRepository = Mockito.mock(ProfessorRepository.class);
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        rankVersionRepository = Mockito.mock(RankVersionRepository.class);

        evaluations = new ArrayList<>();

        evaluations.add(
                new EvaluationDto(2023, Semester.FIRST, "1234567801", "데이터베이스", "김철수", "IT대학", "컴퓨터학부", "교수", 80.03F)
        );

        evaluations.add(
                new EvaluationDto(2023, Semester.FIRST, "8765432101", "인공지능", "김영희", "IT대학", "소프트웨어학부", "교수", 94.02F)
        );

        evaluations.add(
                new EvaluationDto(2023, Semester.FIRST, "2468135701", "컴퓨터그래픽", "홍길동", "IT대학", "글로벌미디어학부", "강사", 97.00F)
        );

        evaluations.add(
                new EvaluationDto(2023, Semester.FIRST, "1357246801", "운영체제", "김철수", "IT대학", "컴퓨터학부", "교수", 86.32F)
        );

        evaluations.add(
                new EvaluationDto(2023, Semester.FIRST, "1357246802", "운영체제", "김철수", "IT대학", "컴퓨터학부", "교수", 83.32F)
        );

        evaluations.add(
                new EvaluationDto(2023, Semester.FIRST, "1357246803", "운영체제", "김철수", "IT대학", "컴퓨터학부", "교수", 88.32F)
        );

        SimpleEvaluationExtract extract = new SimpleEvaluationExtract();
        extract.setEvaluations(evaluations);

        simpleInjectService = new SimpleInjectService(
                courseRepository,
                professorRepository,
                departmentRepository,
                courseProfessorRepository,
                rankVersionRepository,
                extract
        );
    }

    @Test
    @DisplayName("강의 평가 데이터 주입은 오직 한번만 수행되어야 한다.")
    void 데이터_중복저장_방지_테스트() {
        /*
            Given :
            simpleInjectService의 updateEvaluates 메서드는 강의 데이터가
            중복으로 주입되는 경우를 방지하기 위해, RankVersion 이라는 테이블을 만들고
            학년-학기마다 데이터를 한 번에 주입 받도록 한다.

            해당 테스트에서는 2020학년도 1학기 강의 데이터를 주입하는 상황을 가정한다.
            즉, 해당 테스트는 2020학년도 1학기 강의평가 데이터를 주입하는 상황이며 같은 학기를
            또 주입하려고 시도하면 예외가 발생하는 지를 검증하는 테스트이다.
         */
        int year = 2020;
        Semester semester = Semester.FIRST;

        /*
            When :
            updateEvaluates 메서드 내부에서는 rankVersionRepository의
            existsYearAndSemester 메서드를 호출하여 중복 호출 여부를 확인한다.

            RankVersion 테이블에 2020년 1학기 데이터가 주입되어 있지 않은 상태에서
            existsYearAndSemester를 호출하면 false가 반환되어야 한다.
            이후 existsYearAndSemester를 호출하면 2020년 1학기 데이터가 주입되어 있을 것이므로
            true가 반환되어야 한다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemester(year, semester))
                .thenReturn(false)
                .thenReturn(true);

        /*
            Then :
            처음 호출하는 updateEvaluates 메서드는 예외가 발생하지 않고 동작한다.
            이후 호출하는 updateEvaluates 메서드는 예외가 발생하게 된다.
         */
        Assertions.assertDoesNotThrow(() ->
                simpleInjectService.updateEvaluates(year, semester));

        Assertions.assertThrows(EvaluationAlreadyException.class,
                () -> simpleInjectService.updateEvaluates(year, semester));

    }

    @Test
    @DisplayName("강의 평가 데이터가 주입되는 과정에서 학과 정보가 주입된다.")
    void 학과정보_주입_테스트(){
        /*
            Given :
            이 테스트에서 저장되는 학과 정보는 setUp 메서드에서 준비한 강의 평가 데이터에 나오는 학과들이다.
        */
        HashSet<DepartmentDto> savedDepartments;

        savedDepartments = evaluations.stream().map(evaluations ->
                        new DepartmentDto(evaluations.getCollege(), evaluations.getDepartment()))
                .collect(Collectors.toCollection(HashSet::new));

        /*
            When :
            2023학년도 1학기 데이터를 최초로 주입하는 상황임을 가정하고, 그에 맞게 예외 처리가 되지 않도록 설정해준다.
         */

        Mockito.when(rankVersionRepository.existsByYearAndSemester(2023, Semester.FIRST))
                .thenReturn(false);

        simpleInjectService.updateEvaluates(2023, Semester.FIRST);

        /*
            Then :
            미리 준비하여 주입한 데이터 대로 DB에서 조회하는 지와 저장하는 지를 확인한다.
         */
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
            Given :
            이 테스트에서 저장되는 강의 정보는 setUp 메서드에서 준비한 강의 평가 데이터에 나오는 강의들이다.
            evaluationCourses는 모든 과목에 대한 courseDto를 생성하여 저장하고,
            uniqueCourses는 과목 코드를 기준으로 분반을 나누어 courseDto를 생성하여 중복없이 저장한다.
         */
        ArrayList<CourseDto> evaluationCourses =
                (ArrayList<CourseDto>) evaluations.stream().map(evaluations ->
                        new CourseDto(evaluations.getTitle(),
                                evaluations.getYear(),
                                evaluations.getSemester(),
                                evaluations.getCode(),
                                evaluations.getRating()
                        )).collect(Collectors.toList());

        Map<String, CourseDto> uniqueCourses = new HashMap<>();

        for (CourseDto courseDto : evaluationCourses) {
            String slicedCode = courseDto.getCode().substring(0, 8);

            if (!uniqueCourses.containsKey(slicedCode)) uniqueCourses.put(slicedCode, courseDto);

            else {
                CourseDto existingDto = uniqueCourses.get(slicedCode);
                existingDto.setCount(existingDto.getCount() + 1);
                existingDto.setRating(existingDto.getRating() + courseDto.getRating());
            }
        }

        /*
            When :
            2023학년도 1학기 데이터를 최초로 주입하는 상황임을 가정하고, 그에 맞게 예외 처리가 되지 않도록 설정해준다.

            existsByTitleAndOfferedYearAndSemesterAndCode 메서드를 최초로 호출하면 true를 반환하고
            이후 호출은 무조건 false를 반환하도록 설정한다.

            findByTitleAndOfferedYearAndSemesterAndCode 메서드를 호출할 때마다 매개 인자에 맞는 course 객체를
            호출하도록 설정한다.
        */

        Mockito.when(rankVersionRepository.existsByYearAndSemester(2023, Semester.FIRST))
                .thenReturn(false);

        for(CourseDto courseDto : uniqueCourses.values()) {
            String slicedCode = courseDto.getCode().substring(0, 8);

            Mockito.when(courseRepository.existsByTitleAndOfferedYearAndSemesterAndCode(
                            courseDto.getTitle(),
                            courseDto.getYear(),
                            courseDto.getSemester(),
                            slicedCode
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
                    slicedCode
            )).thenReturn(findCourse);
        }

        simpleInjectService.updateEvaluates(2023, Semester.FIRST);

        /*
            Then :
            미리 준비하여 주입한 데이터의 수만큼 저장이 이루어지는 지를 확인한다.
            또, existsByTitleAndOfferedYearAndSemesterAndCode 메서드와
            findByTitleAndOfferedYearAndSemesterAndCode 메서드가 강의평가 데이터에 맞게
            호출되는 지를 확인한다.
        */
        Mockito.verify(courseRepository,
                        Mockito.times(evaluationCourses.size()))
                .save(Mockito.any(Course.class));

        for(CourseDto courseDto : evaluationCourses) {
            String slicedCode = courseDto.getCode().substring(0, 8);
            int count = uniqueCourses.get(slicedCode).getCount();

            Mockito.verify(courseRepository,
                            Mockito.times(count))
                    .existsByTitleAndOfferedYearAndSemesterAndCode(
                            courseDto.getTitle(),
                            courseDto.getYear(),
                            courseDto.getSemester(),
                            slicedCode);
        }

        for(CourseDto courseDto : uniqueCourses.values()){
            if(courseDto.getCount() > 1){
                String slicedCode = courseDto.getCode().substring(0, 8);
                int offset = courseDto.getCount() - 1;

                Mockito.verify(courseRepository,
                        Mockito.times(offset))
                        .findByTitleAndOfferedYearAndSemesterAndCode(
                            courseDto.getTitle(),
                            courseDto.getYear(),
                            courseDto.getSemester(),
                            slicedCode);
            }
        }
    }

    @Test
    @DisplayName("강의 평가 데이터가 주입되는 과정에서 교수 정보가 주입된다.")
    void 교수정보_주입_테스트(){
        /*
            Given :
            이 테스트에서 저장되는 교수 정보는 setUp 메서드에서 준비한 강의 평가 데이터에 나오는 교수들이다.
         */
        ArrayList<ProfessorDto> evaluationProfessors =
                (ArrayList<ProfessorDto>) evaluations.stream().map(evaluations ->
                        new ProfessorDto(evaluations.getProfessorName(),
                                evaluations.getCollege(),
                                evaluations.getDepartment(),
                                evaluations.getPosition()
                        )).collect(Collectors.toList());
        /*
            When :
            2023학년도 1학기 데이터를 최초로 주입하는 상황임을 가정하고, 그에 맞게 예외 처리가 되지 않도록 설정해준다.
         */
        Mockito.when(rankVersionRepository.existsByYearAndSemester(2023, Semester.FIRST))
                .thenReturn(false);

        long mockId = 1;

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


        simpleInjectService.updateEvaluates(2023, Semester.FIRST);

        /*
            Then :
            교수가 이미 저장 되어있는 지를 확인하는 조회가 준비한 데이터 크기만큼 이루어지는 지를 검증한다.
            교수를 저장하기 위해 학과 정보를 가져오는 데이터가 준비한 데이터 중에 중복을 제외한 만큼 이루어지는 지를 검증한다.
            DB에 교수를 저장하는 메서드가 준비한 데이터 중에 중복을 제외한 만큼 이루어지는 지를 검증한다.
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
}
