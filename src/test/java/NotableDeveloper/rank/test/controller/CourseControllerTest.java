package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.CourseController;
import NotableDeveloper.rank.domain.dto.CourseDetailDto;
import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.CourseHistoryDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.exceptiion.CourseNotFoundException;
import NotableDeveloper.rank.domain.request.GetCourseResponse;
import NotableDeveloper.rank.domain.response.GetCoursesResponse;
import NotableDeveloper.rank.service.CourseService;
import NotableDeveloper.rank.test.data.RankData;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Test
    void 강의_아이디_검색_성공_테스트() throws Exception {
        int courseId = 1;

        CourseProfessor findByCourseId = data.getCourseProfessors().stream()
                .filter(cp -> cp.getCourse().getId().equals((long) courseId))
                .findFirst()
                .orElse(null);

        Course c = findByCourseId.getCourse();
        Professor p = findByCourseId.getProfessor();

        List<Course> findAllByCourseCode = data.getCourseProfessors().stream()
                .filter(cp -> cp.getCourse().getCode() == c.getCode())
                .map(cp -> {
                    Course course = cp.getCourse();
                    return course;
                }).collect(Collectors.toList());

        List<CourseHistoryDto> history = findAllByCourseCode.stream()
                .map(course -> CourseHistoryDto.builder()
                        .courseId(course.getId())
                        .tier(course.getTier())
                        .year(course.getOfferedYear())
                        .semester(course.getSemester())
                        .build()
                ).collect(Collectors.toList());

        ProfessorDto professorDto = ProfessorDto.builder()
                .professorId(p.getId())
                .name(p.getName())
                .department(p.getDepartment().getOriginalName())
                .position(p.getPosition())
                .tier(p.getTier())
                .build();

        CourseDetailDto courseDetailDto = CourseDetailDto.builder()
                .courseId(c.getId())
                .code(c.getCode())
                .semester(c.getSemester())
                .year(c.getOfferedYear())
                .courseTier(c.getTier())
                .professor(professorDto)
                .history(history)
                .build();

        String responseBody = objectMapper.writeValueAsString(
                GetCourseResponse.builder()
                        .detailCourse(courseDetailDto)
                        .build());

        Mockito.when(courseService.getCourseById((long) courseId))
                .thenReturn(courseDetailDto);

        mockMvc.perform(get("/course/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        verify(courseService).getCourseById((long) courseId);
    }
}
