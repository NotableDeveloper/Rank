package NotableDeveloper.rank.domain.request;

import NotableDeveloper.rank.domain.enums.Semester;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TierUpdateRequest {
    int year;
    Semester semester;
}
