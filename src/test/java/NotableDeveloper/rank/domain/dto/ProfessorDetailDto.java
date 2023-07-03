package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.hash;

@Getter
@Setter
@Builder
public class ProfessorDetailDto {
    String name;
    String college;
    String department;
    String position;
    Tier tier;

    @Override
    public boolean equals(Object object){
        ProfessorDetailDto professorDto = (ProfessorDetailDto) object;

        return this.getName() == professorDto.getName() &&
                this.getDepartment() == professorDto.getDepartment() &&
                this.getPosition() == professorDto.getPosition();
    }

    @Override
    public int hashCode(){
        return hash(this.getName(), this.getCollege(), this.getDepartment(), this.getPosition());
    }

}
