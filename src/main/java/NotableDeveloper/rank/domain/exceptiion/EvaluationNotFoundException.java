package NotableDeveloper.rank.domain.exceptiion;

public class EvaluationNotFoundException extends RuntimeException{
    public EvaluationNotFoundException(){
        super("강의 평가가 주입되지 않았습니다.");
    }
}
