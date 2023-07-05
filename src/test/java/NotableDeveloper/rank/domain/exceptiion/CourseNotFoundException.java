package NotableDeveloper.rank.domain.exceptiion;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(){
        super("해당하는 강의를 찾을 수 없습니다.");
    }
}
