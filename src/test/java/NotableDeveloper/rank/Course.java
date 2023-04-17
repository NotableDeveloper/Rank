package NotableDeveloper.rank;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
}
