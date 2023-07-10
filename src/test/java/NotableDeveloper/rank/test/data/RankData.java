package NotableDeveloper.rank.test.data;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.enums.Tier;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class RankData {
    ArrayList<Course> courses;
    ArrayList<Professor> professors;
    ArrayList<CourseProfessor> courseProfessors;
    ArrayList<Department> departments;

    public RankData(){
        courses = new ArrayList<>();
        professors = new ArrayList<>();
        courseProfessors = new ArrayList<>();
        departments = new ArrayList<>();

        setUp();
    }

    private void setUp(){
        String[] title = {"객체지향 프로그래밍", "시스템 프로그래밍", "데이터베이스", "운영체제"};
        String[] code = {"12345678", "50301870", "50321144", "24681357"};
        float[] rating = {92.00F, 91.00F, 99.00F, 90.01F};

        for(int i = 0; i < title.length; i++) {
            Long id = (long) (i + 1);

            Course c = Course.builder()
                    .title(title[i])
                    .code(code[i])
                    .year(2023)
                    .semester(Semester.FIRST)
                    .rating(rating[i])
                    .build();

            c.setId(id);
            c.setTier(Tier.A);
            courses.add(c);
        }

        Department computer = Department.builder()
                .college("IT대학")
                .originalName("컴퓨터학부")
                .build();

        computer.setId(1L);
        computer.setShortenedName("컴퓨터");

        Department soft = Department.builder()
                .college("IT대학")
                .originalName("소프트웨어학부")
                .build();

        soft.setId(2L);
        soft.setShortenedName("소프트");

        departments.add(computer);
        departments.add(soft);

        Professor professorKim = Professor.builder()
                .name("김철수")
                .college("IT대학")
                .department(computer)
                .position("교수")
                .build();
        professorKim.setId(1L);
        professorKim.setTier(Tier.A_PLUS);

        Professor professorHong = Professor.builder()
                .name("홍길동")
                .college("IT대학")
                .department(soft)
                .position("강사")
                .build();
        professorHong.setId(2L);
        professorHong.setTier(Tier.A_PLUS);

        Professor professorPark = Professor.builder()
                .name("박철수")
                .college("IT대학")
                .department(soft)
                .position("강사")
                .build();
        professorPark.setId(3L);
        professorPark.setTier(Tier.A_PLUS);

        Professor professorJoo = Professor.builder()
                .name("주성진")
                .college("IT대학")
                .department(computer)
                .position("강사")
                .build();
        professorJoo.setId(4L);
        professorJoo.setTier(Tier.A_PLUS);

        professors.add(professorKim);
        professors.add(professorHong);
        professors.add(professorPark);
        professors.add(professorJoo);

        for(int i = 0; i < title.length; i++){
            CourseProfessor cp = CourseProfessor.builder()
                    .course(courses.get(i))
                    .professor(professors.get(i))
                    .build();

            cp.setId((long) (i + 1));

            courseProfessors.add(cp);
        }
    }
}
