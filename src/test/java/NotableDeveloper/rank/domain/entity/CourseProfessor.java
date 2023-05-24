package NotableDeveloper.rank.domain.entity;

import javax.persistence.*;

@Entity
public class CourseProfessor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "courseId")
    Course course;

    @ManyToOne
    @JoinColumn(name = "professorId")
    Professor professor;
}
