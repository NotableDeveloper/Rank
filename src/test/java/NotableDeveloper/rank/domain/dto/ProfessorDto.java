package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfessorDto {
    Long professorId;
    String name;
    String department;
    String position;
    Tier tier;
}
