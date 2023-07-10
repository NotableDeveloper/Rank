package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.service.ProfessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professors")
@Setter
@Getter
public class ProfessorController {
    @Autowired
    ProfessorService professorService;

    static ObjectMapper objectMapper = new ObjectMapper();
}
