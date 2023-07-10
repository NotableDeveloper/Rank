package NotableDeveloper.rank.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DepartmentDto {
    Long departmentId;
    String college;
    String originalName;
    String shortenedName;
}
