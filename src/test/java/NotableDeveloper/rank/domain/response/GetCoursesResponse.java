package NotableDeveloper.rank.domain.response;

import NotableDeveloper.rank.domain.dto.CourseDto;
import lombok.Builder;
import java.util.List;

@Builder
public class GetCoursesResponse{
    List<CourseDto> courses;
}
