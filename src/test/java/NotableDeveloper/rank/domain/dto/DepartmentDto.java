package NotableDeveloper.rank.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.hash;

@Getter
@Setter
public class DepartmentDto {
    String college;
    String originalName;
    String shortenedName;

    @Builder
    public DepartmentDto(String college, String originalName){
        this.college = college;
        this.originalName = originalName;
        this.shortenedName = "";
    }

    @Override
    public boolean equals(Object object){
        DepartmentDto target = (DepartmentDto) object;

        return
                this.college == target.college
                && this.originalName == target.originalName
                && this.shortenedName == target.shortenedName;
    }

    @Override
    public int hashCode(){
        return hash(this.college, this.originalName, this.shortenedName);
    }
}
