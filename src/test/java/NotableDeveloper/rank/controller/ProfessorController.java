package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.response.GetProfessorResponse;
import NotableDeveloper.rank.domain.response.GetProfessorsResponse;
import NotableDeveloper.rank.service.ProfessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")
@Setter
@Getter
public class ProfessorController {
    @Autowired
    ProfessorService professorService;

    static ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "/honor", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getHonorProfessors(){
        try{
            List<ProfessorDto> response = professorService.getHonorProfessors();
            String responseBody = objectMapper.writeValueAsString(response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);

        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity searchProfessorsByName(@RequestParam String name){
        try{
            GetProfessorsResponse response = GetProfessorsResponse.builder()
                    .professors(professorService.getProfessorsByName(name))
                    .build();

            String responseBody = objectMapper.writeValueAsString(response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);

        } catch(Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/department/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity searchProfessorsByDepartment(@PathVariable int departmentId){
        try{
            GetProfessorsResponse response = GetProfessorsResponse.builder()
                    .professors(professorService.getProfessorsByDepartment((long) departmentId))
                    .build();

            String responseBody = objectMapper.writeValueAsString(response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);

        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{professorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity searchProfessorById(@PathVariable int professorId){
        try{
            GetProfessorResponse response = GetProfessorResponse.builder()
                    .detailProfessor(professorService.getProfessorById((long) professorId))
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
