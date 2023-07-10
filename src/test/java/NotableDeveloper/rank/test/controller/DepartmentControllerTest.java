package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.DepartmentController;
import NotableDeveloper.rank.service.DepartmentService;
import NotableDeveloper.rank.test.data.RankData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc
public class DepartmentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DepartmentService departmentService;

    static ObjectMapper objectMapper = new ObjectMapper();

    static RankData rankData = new RankData();
}
