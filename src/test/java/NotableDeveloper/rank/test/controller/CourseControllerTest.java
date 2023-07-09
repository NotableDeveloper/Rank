package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.CourseController;
import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.domain.response.GetCoursesResponse;
import NotableDeveloper.rank.service.CourseService;
import NotableDeveloper.rank.test.data.RankData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    static ObjectMapper objectMapper = new ObjectMapper();

    static RankData data = new RankData();

    @Test
    void 강의명_검색_실패_테스트() throws Exception {
        String title = "!@#$";

        doThrow(CourseNotFoundException.class).when(courseService).getCourseByTitle(title);

        mockMvc.perform(get("/course")
                        .param("title", title))
                .andExpect(status().isInternalServerError());

        verify(courseService).getCourseByTitle(title);
    }

    @Test
    void 강의명_검색_성공_테스트() throws Exception {
        String title = "프로그래밍";

        List<CourseDto> coursesByProgramming = data.getCourseProfessors().stream()
                .filter(cp -> cp.getCourse().getTitle().contains(title))
                .map(cp -> {
                    Course c = cp.getCourse();

                    return CourseDto.builder()
                            .courseId(c.getId())
                            .title(c.getTitle())
                            .tier(c.getTier())
                            .year(c.getOfferedYear())
                            .semester(c.getSemester())
                            .professor(cp.getProfessor().getName())
                            .department(cp.getProfessor().getDepartment().getOriginalName())
                            .build();
                }).collect(Collectors.toList());

        String responseBody = objectMapper.writeValueAsString(
                GetCoursesResponse.builder()
                        .courses(coursesByProgramming)
                        .build());

        Mockito.when(courseService.getCourseByTitle(title))
                .thenReturn(coursesByProgramming);

        mockMvc.perform(get("/course")
                        .param("title", title))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        verify(courseService).getCourseByTitle(title);
    }
}
