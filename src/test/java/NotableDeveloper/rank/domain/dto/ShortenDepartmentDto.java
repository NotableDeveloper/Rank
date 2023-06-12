package NotableDeveloper.rank.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShortenDepartmentDto {
    String originalName;
    String shortenedName;
}
