package NotableDeveloper.rank.domain.entity;

import NotableDeveloper.rank.domain.enums.Semester;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;

@Getter
@Setter
@Entity
public class RankVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    int year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Semester semester;

    @Column(nullable = false)
    boolean injected;

    @Column(nullable = false)
    boolean classifiedCourse;

    @Column(nullable = false)
    boolean classifiedProfessor;

    public RankVersion(int year, Semester semester) {
        this.year = year;
        this.semester = semester;
        this.injected = false;
        this.classifiedCourse = false;
        this.classifiedProfessor = false;
    }
}
