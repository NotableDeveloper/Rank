package NotableDeveloper.rank.test.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest
@AutoConfigureMockMvc
public class DataInjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void MVC_테스트() throws Exception{
        String requestBody = "{\"year\": 2023, \"semester\": \"FIRST\"}";

        mockMvc.perform(put("/data/evaluates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
