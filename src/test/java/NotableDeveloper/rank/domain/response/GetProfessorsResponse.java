package NotableDeveloper.rank.domain.response;

import NotableDeveloper.rank.domain.dto.ProfessorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetProfessorsResponse {
    List<ProfessorDto> professors;
}
