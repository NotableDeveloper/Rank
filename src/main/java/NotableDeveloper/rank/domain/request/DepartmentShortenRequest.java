package NotableDeveloper.rank.domain.request;

import NotableDeveloper.rank.domain.enums.Semester;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentShortenRequest {
    int year;
    Semester semester;
    List<DepartmentShortName> nameSet;
}
