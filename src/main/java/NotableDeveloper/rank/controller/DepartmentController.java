package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.domain.response.GetDepartmentsResponse;
import NotableDeveloper.rank.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getAllDepartments(){
        try{
            GetDepartmentsResponse response = GetDepartmentsResponse.builder()
                    .departments(departmentService.getDepartments())
                    .build();

            String responseBody = objectMapper.writeValueAsString(response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);

        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
