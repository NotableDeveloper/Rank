package NotableDeveloper.rank.test.service.ProfessorService;

import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import NotableDeveloper.rank.domain.exceptiion.ProfessorNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import NotableDeveloper.rank.service.ProfessorService;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfessorServiceTest {
    ProfessorService professorService;

    @Mock
    ProfessorRepository professorRepository;

    @Mock
    CourseProfessorRepository courseProfessorRepository;

    static RankData rankData = new RankData();

    @BeforeEach
    void setUp(){
        professorService = new ProfessorService();
        professorRepository = Mockito.mock(ProfessorRepository.class);
        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        professorService.setProfessorRepository(professorRepository);
        professorService.setCourseProfessorRepository(courseProfessorRepository);
    }

    @Test
    @DisplayName("특정 키워드로 교수를 검색을 했을 때, 결과가 없다면 예외가 발생한다.")
    void 교수_이름검색_실패_테스트(){
        String name = "철수";

        Mockito.when(professorRepository.findAllByNameContains(Mockito.any()))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(ProfessorNotFoundException.class,
                () -> professorService.getProfessorsByName(name));
    }

    @Test
    @DisplayName("특정 키워드로 교수를 검색을 했을 때, 결과가 올바른 지를 검증한다.")
    void 교수_이름검색_성공_테스트(){
        String name = "철수";

        List<Professor> findAllProfessorByName = rankData.getCourseProfessors().stream()
                .map(p -> p.getProfessor())
                .collect(Collectors.toList());

        Mockito.when(professorRepository.findAllByNameContains(name))
                .thenReturn(findAllProfessorByName);

        List<ProfessorDto> exceptedProfessors = findAllProfessorByName.stream()
                .map(p -> ProfessorDto.builder()
                        .professorId(p.getId())
                        .tier(p.getTier())
                        .department(p.getDepartment().getOriginalName())
                        .name(p.getName())
                        .position(p.getPosition())
                        .build())
                .collect(Collectors.toList());

        List<ProfessorDto> findProfessors = professorService.getProfessorsByName(name);

        for(int i = 0; i < findProfessors.size(); i++){
            ProfessorDto excepted = exceptedProfessors.get(i);
            ProfessorDto find = findProfessors.get(i);

            Assertions.assertEquals(excepted.getProfessorId(), find.getProfessorId());
            Assertions.assertEquals(excepted.getName(), find.getName());
            Assertions.assertEquals(excepted.getPosition(), find.getPosition());
            Assertions.assertEquals(excepted.getDepartment(), find.getDepartment());
            Assertions.assertEquals(excepted.getTier(), find.getTier());
        }
    }

    @Test
    @DisplayName("특정 학과에 소속된 교수들을 검색할 때, 결과가 없다면 예외가 발생한다.")
    void 교수_학과ID_검색_실패_테스트(){
        Long departmentId = 9999L;

        Mockito.when(professorRepository.findAllByDepartment_Id(departmentId))
                .thenReturn(new ArrayList<>());

        Assertions.assertThrows(ProfessorNotFoundException.class,
                () -> professorService.getProfessorsByDepartment(departmentId));
    }
}
