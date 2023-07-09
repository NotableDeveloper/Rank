package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.service.CourseService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
@Getter
@Setter
public class CourseController {
    @Autowired
    CourseService courseService;
}
