package NotableDeveloper.rank.test.controller;

import NotableDeveloper.rank.controller.ProfessorController;
import NotableDeveloper.rank.domain.dto.CourseHistoryDto;
import NotableDeveloper.rank.domain.dto.ProfessorDetailDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.exceptiion.ProfessorNotFoundException;
import NotableDeveloper.rank.domain.response.GetProfessorResponse;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

        mockMvc.perform(get("/professor/honor"))
                .andExpect(status().isOk());

        verify(professorService).getHonorProfessors();
    }

    @Test
    void 교수_검색_실패_테스트() throws Exception{
        String name = "@#!#@$";
        int departmentId = 1;
        int professorId = 2;

        doThrow(ProfessorNotFoundException.class)
                .when(professorService)
                .getProfessorsByName(name);

        doThrow(ProfessorNotFoundException.class)
                .when(professorService)
                .getProfessorsByDepartment((long) departmentId);

        doThrow(ProfessorNotFoundException.class)
                .when(professorService)
                .getProfessorById((long) professorId);

        mockMvc.perform(get("/professor")
                .param("name", name))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(get("/professor/department/{departmentId}", departmentId))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(get("/professor/{professorId}", professorId))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void 교수명_검색_성공_테스트() throws Exception {
        String name = "철수";

        List<ProfessorDto> findAllByName = data.getCourseProfessors()
                .stream()
                .filter(cp -> cp.getProfessor().getName().equals(name))
                .map(cp -> {
                    Professor p = cp.getProfessor();

                    return ProfessorDto.builder()
                            .professorId(p.getId())
                            .tier(p.getTier())
                            .name(p.getName())
                            .position(p.getPosition())
                            .department(p.getDepartment().getOriginalName())
                            .build();
                })
                .collect(Collectors.toList());

        Mockito.when(professorService.getProfessorsByName(name))
                .thenReturn(findAllByName);

        String responseBody = objectMapper.writeValueAsString(
                GetProfessorsResponse.builder()
                        .professors(findAllByName)
                        .build());

        mockMvc.perform(get("/professor")
                .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        verify(professorService).getProfessorsByName(name);
    }

    @Test
    void 학과별_교수검색_성공_테스트() throws Exception {
        int departmentId = 1;

        List<ProfessorDto> findAllByDepartment = data.getCourseProfessors()
                .stream()
                .filter(cp -> cp.getProfessor()
                        .getDepartment()
                        .getId()
                        .equals((long) departmentId))
                .map(cp -> {
                    Professor p = cp.getProfessor();

                    return ProfessorDto.builder()
                            .professorId(p.getId())
                            .tier(p.getTier())
                            .name(p.getName())
                            .position(p.getPosition())
                            .department(p.getDepartment().getOriginalName())
                            .build();
                })
                .collect(Collectors.toList());

        Mockito.when(professorService.getProfessorsByDepartment((long) departmentId))
                .thenReturn(findAllByDepartment);

        String responseBody = objectMapper.writeValueAsString(
                GetProfessorsResponse.builder()
                        .professors(findAllByDepartment)
                        .build());

        mockMvc.perform(get("/professor/department/{departmentId}", departmentId))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        verify(professorService).getProfessorsByDepartment((long) departmentId);
    }

    @Test
    void 교수_아이디검색_성공_테스트() throws Exception {
        int professorId = 1;

        ArrayList<CourseProfessor> cpByProfessorId = (ArrayList<CourseProfessor>)
                data.getCourseProfessors().stream()
                        .filter(cp -> cp.getProfessor()
                                .getId()
                                .equals((long) professorId))
                        .collect(Collectors.toList());

        List<CourseHistoryDto> history = cpByProfessorId.stream()
                .map(cp -> {
                    Course c = cp.getCourse();

                    return CourseHistoryDto.builder()
                            .title(c.getTitle())
                            .year(c.getOfferedYear())
                            .semester(c.getSemester())
                            .courseId(c.getId())
                            .tier(c.getTier())
                            .build();
                })
                .collect(Collectors.toList());

        Professor p = cpByProfessorId.get(0).getProfessor();

        ProfessorDetailDto professorDetailDto = ProfessorDetailDto.builder()
                .offeredCourses(history)
                .professorTier(p.getTier())
                .name(p.getName())
                .position(p.getPosition())
                .courseCount(history.size())
                .college(p.getCollege())
                .professorId(p.getId())
                .department(p.getDepartment().getOriginalName())
                .build();

        String responseBody = objectMapper.writeValueAsString(
                GetProfessorResponse.builder()
                        .detailProfessor(professorDetailDto)
                        .build());

        Mockito.when(professorService.getProfessorById((long) professorId))
                .thenReturn(professorDetailDto);

        mockMvc.perform(get("/professor/{professorId}", professorId))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        verify(professorService).getProfessorById((long) professorId);
    }
}
