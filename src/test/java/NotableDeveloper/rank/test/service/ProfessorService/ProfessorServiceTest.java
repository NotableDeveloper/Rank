package NotableDeveloper.rank.test.service.ProfessorService;

import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import NotableDeveloper.rank.service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;

public class ProfessorServiceTest {
    ProfessorService professorService;

    @Mock
    ProfessorRepository professorRepository;

    @Mock
    CourseProfessorRepository courseProfessorRepository;

    @BeforeEach
    void setUp(){
        professorService = new ProfessorService();
        professorRepository = Mockito.mock(ProfessorRepository.class);
        courseProfessorRepository = Mockito.mock(CourseProfessorRepository.class);
        professorService.setProfessorRepository(professorRepository);
        professorService.setCourseProfessorRepository(courseProfessorRepository);
    }
}
