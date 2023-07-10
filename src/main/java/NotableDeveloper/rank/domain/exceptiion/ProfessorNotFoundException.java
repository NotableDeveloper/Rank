package NotableDeveloper.rank.domain.exceptiion;

public class ProfessorNotFoundException extends RuntimeException{
    public ProfessorNotFoundException(){
        super("해당하는 교수를 찾을 수 없습니다.");
    }
}
