package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department")
@Getter
@Setter
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    static ObjectMapper objectMapper = new ObjectMapper();
}
