package NotableDeveloper.rank.domain.response;

import NotableDeveloper.rank.domain.dto.DepartmentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetDepartmentsResponse {
    List<DepartmentDto> departments;
}
