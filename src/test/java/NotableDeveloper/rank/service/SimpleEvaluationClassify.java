package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.service.function.EvaluationClassify;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter
@Setter
public class SimpleEvaluationClassify implements EvaluationClassify {
    List<CourseDto> uniqueCourses;

    public SimpleEvaluationClassify(List<CourseDto> courses) {
        uniqueCourses = new ArrayList<>();
        distinct(courses);
    }

    void distinct(List<CourseDto> courses) {
        Map<List<Object>, CourseDto> courseMap = new HashMap<>();

        courses.forEach(course -> {
            List<Object> key = Arrays.asList(course.getTitle(),
                    course.getYear(),
                    course.getCode(),
                    course.getSemester());

            if (courseMap.containsKey(key)) {
                CourseDto existingCourse = courseMap.get(key);
                existingCourse.setCount(existingCourse.getCount() + 1);
                existingCourse.setRating(existingCourse.getRating() + course.getRating());
            }

            else {
                courseMap.put(key, course);
                uniqueCourses.add(course);
            }
        });
    }


    @Override
    public void classifyEvaluation() {

    }
}
