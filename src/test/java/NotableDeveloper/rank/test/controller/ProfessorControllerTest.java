package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.ProfessorController;
import NotableDeveloper.rank.service.ProfessorService;
import NotableDeveloper.rank.test.data.RankData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProfessorController.class)
@AutoConfigureMockMvc
public class ProfessorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProfessorService professorService;

    static ObjectMapper objectMapper = new ObjectMapper();

    static RankData data = new RankData();
}
