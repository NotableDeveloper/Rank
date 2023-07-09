package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.domain.response.GetCoursesResponse;
import NotableDeveloper.rank.service.CourseService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
@Getter
@Setter
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping
    ResponseEntity searchCoursesByTitle(@RequestParam String title) {
        try{
            GetCoursesResponse response = GetCoursesResponse.builder()
                .courses(courseService.getCourseByTitle(title))
                .build();

            return new ResponseEntity(response, HttpStatus.OK);

        } catch (Exception e){
           return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
