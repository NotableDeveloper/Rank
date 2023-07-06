package NotableDeveloper.rank.test.service.DepartmentService;

import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.service.DepartmentService;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DepartmentServiceTest {
    DepartmentService departmentService;
    @Mock
    DepartmentRepository departmentRepository;

    static RankData rankData = new RankData();

    @BeforeEach
    void setUp(){
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        departmentService.setDepartmentRepository(departmentRepository);
    }
}
