package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.DepartmentController;
import NotableDeveloper.rank.domain.dto.DepartmentDto;
import NotableDeveloper.rank.domain.response.GetDepartmentsResponse;
import NotableDeveloper.rank.service.DepartmentService;
import NotableDeveloper.rank.test.data.RankData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc
public class DepartmentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DepartmentService departmentService;

    static ObjectMapper objectMapper = new ObjectMapper();

    static RankData rankData = new RankData();

    @Test
    void 학과목록_불러오기_테스트() throws Exception {
        List<DepartmentDto> departments = rankData.getDepartments()
                .stream()
                .map(d -> DepartmentDto.builder()
                        .college(d.getCollege())
                        .departmentId(d.getId())
                        .originalName(d.getOriginalName())
                        .shortenedName(d.getShortenedName())
                        .build())
                .collect(Collectors.toList());

        Mockito.when(departmentService.getDepartments())
                .thenReturn(departments);

        String responseBody = objectMapper.writeValueAsString(
                GetDepartmentsResponse.builder()
                        .departments(departments)
                        .build());

        mockMvc.perform(get("/department"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }
}
