package NotableDeveloper.rank.domain.exceptiion;

import org.junit.jupiter.api.function.Executable;

public class EvaluationAlreadyException extends RuntimeException {
    public EvaluationAlreadyException(){
        super("이미 주입되어 있는 데이터입니다.");
    }
}
