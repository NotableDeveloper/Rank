package NotableDeveloper.rank.domain.response;

import NotableDeveloper.rank.domain.dto.ProfessorDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetProfessorResponse {
    ProfessorDetailDto detailProfessor;
}
