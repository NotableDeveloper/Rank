package NotableDeveloper.rank.service.implement;

import NotableDeveloper.rank.domain.dto.CourseDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.enums.Tier;
import NotableDeveloper.rank.service.function.EvaluationClassify;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter
@Setter
public class SimpleEvaluationClassify implements EvaluationClassify {
    List<CourseDto> uniqueCourses;
    List<ProfessorDto> uniqueProfessors;

    public SimpleEvaluationClassify() {
        uniqueCourses = new ArrayList<>();
        uniqueProfessors = new ArrayList<>();
    }

    @Override
    public void classifyCourse(List<CourseDto> courses) {
        distinctCourses(courses);
        calculateCoursePercentage();
        assignCourseTier();
    }

    @Override
    public void classifyProfessor(List<ProfessorDto> professors) {
        distinctProfessor(professors);
        calculateProfessorPercentage();
        assignProfessorTier();
    }

    private void distinctCourses(List<CourseDto> courses) {
        Map<List<Object>, CourseDto> courseMap = new HashMap<>();

        courses.forEach(course -> {
            List<Object> key = Arrays.asList(
                    course.getTitle(),
                    course.getYear(),
                    course.getCode(),
                    course.getSemester());

            if (courseMap.containsKey(key)) {
                CourseDto existingCourse = courseMap.get(key);
                existingCourse.setCount(existingCourse.getCount() + 1);
                existingCourse.setRating(existingCourse.getRating() + course.getRating());
            }

            else {
                courseMap.put(key, course);
                uniqueCourses.add(course);
            }
        });
    }

    private void distinctProfessor(List<ProfessorDto> professors){
        Map<List<Object>, ProfessorDto> professorMap = new HashMap<>();

        professors.forEach(professor -> {
            List<Object> key = Arrays.asList(
                    professor.getName(),
                    professor.getCollege(),
                    professor.getDepartment(),
                    professor.getPosition());

            if(!professorMap.containsKey(key)){
                professorMap.put(key, professor);
                uniqueProfessors.add(professor);
            }
        });
    }

    private void calculateCoursePercentage() {
        for (CourseDto course : uniqueCourses) {
            float average = course.getRating() / course.getCount();
            course.setAverage(average);
        }
    }

    private void calculateProfessorPercentage(){
        for(ProfessorDto professor : uniqueProfessors){
            float average = professor.getRating() / professor.getCount();
            professor.setAverage(average);
        }
    }

    private void assignCourseTier(){
        Collections.sort(uniqueCourses);

        int totalCourses = uniqueCourses.size();

        for (int i = 0; i < totalCourses; i++) {
            float percentage = (float)(i + 1) / totalCourses * 100;

            if(0.0F <= percentage && percentage <= 30.00F)
                uniqueCourses.get(i).setTier(Tier.A);

            else if(30.00F < percentage && percentage <= 70.00F)
                uniqueCourses.get(i).setTier(Tier.B);

            else if(70.00F < percentage && percentage <= 85.00F)
                uniqueCourses.get(i).setTier(Tier.C);

            else
                uniqueCourses.get(i).setTier(Tier.D);
        }
    }

    private void assignProfessorTier(){
        Collections.sort(uniqueProfessors);

        int totalProfessors = uniqueProfessors.size();

        for(int i = 0; i < totalProfessors; i++){
            float percentage = (float)(i + 1) / totalProfessors * 100;

            if(0.0F < percentage && percentage < 10.00F)
                uniqueProfessors.get(i).setTier(Tier.A_PLUS);

            else if(10.00F <= percentage && percentage < 20.00F)
                uniqueProfessors.get(i).setTier(Tier.A);

            else if(20.00F <= percentage && percentage < 30.00F)
                uniqueProfessors.get(i).setTier(Tier.A_MINUS);

            else if(30.0F <= percentage && percentage < 40.00F)
                uniqueProfessors.get(i).setTier(Tier.B_PLUS);

            else if(40.00F <= percentage && percentage < 60.00F)
                uniqueProfessors.get(i).setTier(Tier.B);

            else if(60.00F <= percentage && percentage < 70.00F)
                uniqueProfessors.get(i).setTier(Tier.B_MINUS);

            else if(70.00F <= percentage && percentage < 75.00F)
                uniqueProfessors.get(i).setTier(Tier.C_PLUS);

            else if(75.00F <= percentage && percentage < 80.00F)
                uniqueProfessors.get(i).setTier(Tier.C);

            else if(80.00F <= percentage && percentage < 85.00F)
                uniqueProfessors.get(i).setTier(Tier.C_MINUS);

            else if(85.00F <= percentage && percentage < 90.00F)
                uniqueProfessors.get(i).setTier(Tier.D_PLUS);

            else if(90.00F <= percentage && percentage < 95.00F)
                uniqueProfessors.get(i).setTier(Tier.D);

            else
                uniqueProfessors.get(i).setTier(Tier.D_MINUS);
        }
    }
}