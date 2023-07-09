package NotableDeveloper.rank.domain.response;

import NotableDeveloper.rank.domain.dto.CourseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class GetCoursesResponse{
    List<CourseDto> courses;
}
