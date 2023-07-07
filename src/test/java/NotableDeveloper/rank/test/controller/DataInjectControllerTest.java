package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.DataInjectController;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.request.DepartmentShortenRequest;
import NotableDeveloper.rank.service.SimpleInjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataInjectController.class)
@AutoConfigureMockMvc
public class DataInjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    SimpleInjectService injectService;

    @Test
    void 강의평가_데이터_주입_테스트() throws Exception{
        String requestBody = "{\"year\": 2023, \"semester\": \"FIRST\"}";
        doNothing().when(injectService).saveEvaluates(anyInt(), any(Semester.class));

        mockMvc.perform(put("/data/evaluates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(injectService).saveEvaluates(eq(2023), eq(Semester.FIRST));
    }

    @Test
    void 학과축약이름_갱신_테스트() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        DepartmentShortenRequest request = new DepartmentShortenRequest();
        request.setYear(2023);
        request.setSemester(Semester.FIRST);
        /*
            현재 InjectService에서는 년도와 학기만 입력받고 있기 때문에,
            학과이름 - 축약이름 형태의 데이터는 필요하지 않음.
         */
        request.setNameSet(new ArrayList<>());

        String requestBody = objectMapper.writeValueAsString(request);

        doNothing().when(injectService).saveEvaluates(anyInt(), any(Semester.class));

        mockMvc.perform(put("/data/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(injectService).updateDepartments(eq(2023), eq(Semester.FIRST));
    }
}
