package NotableDeveloper.rank.domain.entity;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import lombok.*;
import javax.persistence.*;

import static java.util.Objects.hash;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int offeredYear;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Semester semester;

    @Column(nullable = false, length = 8)
    private String code;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Column(nullable = false)
    private int count;

    public Course(String title, int year, Semester semester, String code, float rating){
        this.title = title;
        this.offeredYear = year;
        this.semester = semester;
        this.code = code;
        this.rating = rating;
        this.tier = Tier.F;
        this.count = 1;
    }

    @Override
    public boolean equals(Object other){
        return (this.getTitle() == ((Course) other).getTitle() &&
                this.getOfferedYear() == ((Course) other).getOfferedYear() &&
                this.getSemester() == ((Course) other).getSemester() &&
                this.getCode() == ((Course) other).getCode());
    }

    @Override
    public int hashCode(){
        return hash(this.getTitle(), this.offeredYear, this.getSemester(), this.getCode());
    }
}
