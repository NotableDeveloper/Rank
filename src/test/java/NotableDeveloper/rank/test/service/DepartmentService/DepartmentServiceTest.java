package NotableDeveloper.rank.test.service.DepartmentService;

import NotableDeveloper.rank.domain.dto.DepartmentDto;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.service.DepartmentService;
import NotableDeveloper.rank.test.data.RankData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentServiceTest {
    DepartmentService departmentService;
    @Mock
    DepartmentRepository departmentRepository;

    static RankData rankData = new RankData();

    @BeforeEach
    void setUp(){
        departmentService = new DepartmentService();
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        departmentService.setDepartmentRepository(departmentRepository);
    }

    @Test
    @DisplayName("현재 시스템에 저장된 모든 학과 정보를 불러오는 기능을 검증한다.")
    void 모든_학과_불러오기_테스트(){
        List<DepartmentDto> exceptedDepartments = rankData.getDepartments().stream()
                .map(department -> DepartmentDto.builder()
                        .college(department.getCollege())
                        .departmentId(department.getId())
                        .originalName(department.getOriginalName())
                        .shortenedName(department.getShortenedName())
                        .build())
                .collect(Collectors.toList());

        Mockito.when(departmentRepository.findAll())
                .thenReturn(rankData.getDepartments());

        List<DepartmentDto> findDepartments = departmentService.getDepartments();

        for(int i = 0; i < exceptedDepartments.size(); i++){
            DepartmentDto excepted = exceptedDepartments.get(i);
            DepartmentDto find = findDepartments.get(i);

            Assertions.assertEquals(excepted.getDepartmentId(), find.getDepartmentId());
            Assertions.assertEquals(excepted.getCollege(), find.getCollege());
            Assertions.assertEquals(excepted.getOriginalName(), find.getOriginalName());
            Assertions.assertEquals(excepted.getShortenedName(), find.getShortenedName());
        }
    }
}
