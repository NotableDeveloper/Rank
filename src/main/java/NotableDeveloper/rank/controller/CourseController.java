package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.domain.response.GetCourseResponse;
import NotableDeveloper.rank.domain.response.GetCoursesResponse;
import NotableDeveloper.rank.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@Getter
@Setter
public class CourseController {
    @Autowired
    CourseService courseService;

    static ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity searchCoursesByTitle(@RequestParam String title) {
        try{
            GetCoursesResponse response = GetCoursesResponse.builder()
                .courses(courseService.getCourseByTitle(title))
                .build();

            String responseBody = objectMapper.writeValueAsString(response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);

        } catch (Exception e){
           return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity searchCourseByCourseId(@PathVariable int courseId){
        try{
            GetCourseResponse response = GetCourseResponse.builder()
                    .detailCourse(courseService.getCourseById((long) courseId))
                    .build();

            String responseBody = objectMapper.writeValueAsString(response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);

        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
