package NotableDeveloper.rank.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 11)
    private String college;

    @Column(nullable = false, length = 15)
    private String originalName;

    @Column(nullable = false, length = 6)
    private String shortenedName;

    public Department(String college, String originalName, String shortenedName){
        this.college = college;
        this.originalName = originalName;
        this.shortenedName = shortenedName;
    }

    public Department(String college, String originalName){
        this.college = college;
        this.originalName = originalName;
        this.shortenedName = "";
    }
}
