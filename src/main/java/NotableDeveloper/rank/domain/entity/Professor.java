package NotableDeveloper.rank.domain.entity;

import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String college;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    private float average;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Builder
    public Professor(String name, String college, Department department, String position){
        this.name = name;
        this.college = college;
        this.department = department;
        this.position = position;
        this.rating = 0.0F;
        this.tier = Tier.F;
    }
}
