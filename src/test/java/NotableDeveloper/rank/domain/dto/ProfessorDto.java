package NotableDeveloper.rank.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.hash;

@Getter
@Setter
@AllArgsConstructor
public class ProfessorDto {
    String name;
    String college;
    String department;
    String position;

    @Override
    public boolean equals(Object object){
        ProfessorDto professorDto = (ProfessorDto) object;

        return this.getName() == professorDto.getName() &&
                this.getDepartment() == professorDto.getDepartment() &&
                this.getPosition() == professorDto.getPosition();
    }

    @Override
    public int hashCode(){
        return hash(this.getName(), this.getCollege(), this.getDepartment(), this.getPosition());
    }
}

