package NotableDeveloper.rank.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public CourseProfessor(Course course, Professor professor){
        this.course = course;
        this.professor = professor;
    }
}
