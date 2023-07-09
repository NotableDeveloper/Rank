package NotableDeveloper.rank.domain.request;

import NotableDeveloper.rank.domain.dto.CourseDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetCourseResponse {
    CourseDetailDto detailCourse;
}
