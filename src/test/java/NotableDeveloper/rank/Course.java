package NotableDeveloper.rank;

import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Getter
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

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Column(nullable = false)
    private int count;

    public Course(String title, int year, Semester semester, String code){
        this.title = title;
        this.offeredYear = year;
        this.semester = semester;
        this.code = code;
        this.rating = 0.0F;
        this.tier = Tier.F;
        this.count = 1;
    }
}
