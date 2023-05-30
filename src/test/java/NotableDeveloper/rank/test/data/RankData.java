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
    static ArrayList<Course> courses;
    static ArrayList<Professor> professors;
    static ArrayList<CourseProfessor> courseProfessors;
    static ArrayList<Department> departments;

    public RankData(){
        courses = new ArrayList<>();
        professors = new ArrayList<>();
        courseProfessors = new ArrayList<>();
        departments = new ArrayList<>();

        setUp();
    }

    private void setUp(){
        courses.add(new Course("객체지향 프로그래밍", 2023, Semester.FIRST, "12345678", 79.00F));
        courses.add(new Course("객체지향 프로그래밍", 2023, Semester.FIRST, "12345678", 76.00F));
        courses.add(new Course("객체지향 프로그래밍", 2023, Semester.FIRST, "12345678", 82.00F));
        courses.add(new Course("시스템 프로그래밍", 2022, Semester.FIRST, "50301870", 76.00F));
        courses.add(new Course("데이터베이스", 2022, Semester.FIRST, "50321144", 82.00F));

        Department computer = new Department("IT대학", "컴퓨터학부", "컴퓨터");
        Department soft = new Department("IT대학", "소프트웨어학부", "소프트");

        departments.add(computer);
        departments.add(soft);

        professors.add(new Professor("김철수", "IT대학", computer, "교수"));
        professors.add(new Professor("홍길동", "IT대학", soft, "강사"));

        /*
            2023년 1학기 객체지향 프로그래밍 강의는 컴퓨터학부의 김철수 교수가 담당하였다.
            2022년 1학기 시스템 프로그래밍, 데이터베이스 강의는 소프트웨어학부의 홍길동 교수가 담당하였다.
         */
        for(int i = 0; i < courses.size(); i++){
            if(i < 3)
                courseProfessors.add(new CourseProfessor(courses.get(i), professors.get(0)));

            else courseProfessors.add(new CourseProfessor(courses.get(i), professors.get(1)));
        }
    }
}
