package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.DataInjectController;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.request.DepartmentShortenRequest;
import NotableDeveloper.rank.domain.request.EvaluateRequest;
import NotableDeveloper.rank.domain.request.TierUpdateRequest;
import NotableDeveloper.rank.repository.RankVersionRepository;
import NotableDeveloper.rank.service.SimpleInjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataInjectController.class)
@AutoConfigureMockMvc
public class DataInjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    SimpleInjectService injectService;

    static ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void 강의평가_데이터_주입_테스트() throws Exception{
        EvaluateRequest request = new EvaluateRequest();
        request.setYear(2023);
        request.setSemester(Semester.FIRST);
        String requestBody = objectMapper.writeValueAsString(request);

        doNothing().when(injectService).saveEvaluates(anyInt(), any(Semester.class));

        mockMvc.perform(post("/data/evaluates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(injectService).saveEvaluates(eq(2023), eq(Semester.FIRST));
    }

    @Test
    void 학과축약이름_갱신_테스트() throws Exception {
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

    @Test
    void 강의_등급부여_테스트() throws Exception {
        TierUpdateRequest request = new TierUpdateRequest();
        request.setYear(2023);
        request.setSemester(Semester.FIRST);

        String requestBody = objectMapper.writeValueAsString(request);

        doNothing().when(injectService).updateCourses(anyInt(), any(Semester.class));

        mockMvc.perform(put("/data/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(injectService).updateCourses(eq(2023), eq(Semester.FIRST));
    }

    @Test
    void 교수_등급부여_테스트() throws Exception {
        TierUpdateRequest request = new TierUpdateRequest();
        request.setYear(2023);
        request.setSemester(Semester.FIRST);

        String requestBody = objectMapper.writeValueAsString(request);

        doNothing().when(injectService).updateProfessors(anyInt(), any(Semester.class));

        mockMvc.perform(put("/data/professors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(injectService).updateProfessors(eq(2023), eq(Semester.FIRST));
    }
}
