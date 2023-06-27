package NotableDeveloper.rank.test.data;

import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Semester;
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
        String[] title = {"객체지향 프로그래밍", "객체지향 프로그래밍", "객체지향 프로그래밍", "시스템 프로그래밍", "데이터베이스"};
        String[] code = {"12345678", "12345678", "12345678", "50301870", "50321144"};
        float[] rating = {79.00F, 76.00F, 82.00F, 76.00F, 82.00F};

        for(int i = 0; i < title.length; i++) {
            courses.add(Course.builder()
                    .title(title[i])
                    .code(code[i])
                    .year(2023)
                    .semester(Semester.FIRST)
                    .rating(rating[i])
                    .build());
        }

        Department computer = Department.builder()
                .college("IT대학")
                .originalName("컴퓨터학부")
                .build();

        Department soft = Department.builder()
                .college("IT대학")
                .originalName("소프트웨어학부")
                .build();

        departments.add(computer);
        departments.add(soft);

        professors.add(Professor.builder()
                .name("김철수")
                .college("IT대학")
                .department(computer)
                .position("교수")
                .build());

        professors.add(Professor.builder()
                .name("홍길동")
                .college("IT대학")
                .department(soft)
                .position("강사")
                .build());



        /*
            2023년 1학기 객체지향 프로그래밍 강의는 컴퓨터학부의 김철수 교수가 담당하였다.
            2022년 1학기 시스템 프로그래밍, 데이터베이스 강의는 소프트웨어학부의 홍길동 교수가 담당하였다.
         */
        for(int i = 0; i < courses.size(); i++) {
            CourseProfessor saveCP = CourseProfessor.builder()
                    .course(courses.get(i))
                    .professor(professors.get(0))
                    .build();

            /*
                중복을 제외하고 저장한다.
             */
            if (!courseProfessors.stream().anyMatch(
                alreadyCP -> alreadyCP.getCourse().getCode().equals(saveCP.getCourse().getCode())
            )) {
                courseProfessors.add(saveCP);
            }
        }
    }
}
