package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.ProfessorController;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.response.GetProfessorsResponse;
import NotableDeveloper.rank.service.ProfessorService;
import NotableDeveloper.rank.test.data.RankData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfessorController.class)
@AutoConfigureMockMvc
public class ProfessorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProfessorService professorService;

    static ObjectMapper objectMapper = new ObjectMapper();

    static RankData data = new RankData();

    @Test
    void 명예의_전당_호출_테스트() throws Exception{
        ArrayList<ProfessorDto> emptyDto = new ArrayList<>();

        Mockito.when(professorService.getHonorProfessors())
                        .thenReturn(emptyDto);

        mockMvc.perform(get("/professors/honor"))
                .andExpect(status().isOk());

        verify(professorService).getHonorProfessors();
    }
}
