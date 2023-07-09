package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.CourseController;
import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 강의명_검색_실패_테스트() throws Exception {
        String title = "!@#$";

        doThrow(CourseNotFoundException.class).when(courseService).getCourseByTitle(title);

        mockMvc.perform(get("/course/")
                        .param("title", title))
                .andExpect(status().isInternalServerError());

        verify(courseService).getCourseByTitle(title);
    }
}
