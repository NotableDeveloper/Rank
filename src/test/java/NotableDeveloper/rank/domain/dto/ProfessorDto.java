package NotableDeveloper.rank.domain.dto;

import NotableDeveloper.rank.domain.enums.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.hash;

@Getter
@Setter
public class ProfessorDto implements Comparable<ProfessorDto> {
    String name;
    String college;
    String department;
    String position;
    int count;
    float rating;
    float average;
    Tier tier;

    @Builder
    public ProfessorDto(String name, String college, String department, String position){
        this.name = name;
        this.college = college;
        this.department = department;
        this.position = position;
        this.count = 0;
        this.rating = 0.0F;
        this.average = 0.0F;
        this.tier = Tier.U;
    }

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

    @Override
    public int compareTo(ProfessorDto o) {
        return 0;
    }
}

