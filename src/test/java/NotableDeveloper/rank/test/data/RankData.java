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
        
    }
}
