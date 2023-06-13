package NotableDeveloper.rank.domain.exceptiion;

public class ClassifyAlreadyException extends RuntimeException{
    public ClassifyAlreadyException(){
        super("이미 등급이 부여된 데이터입니다.");
    }
}
