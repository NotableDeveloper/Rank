package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.enums.Semester;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public abstract class DataInjectService  {
    // 크롤링, 파일 등으로 강의평가 데이터를 읽어서 DB에 저장하는 메서드.
    public abstract void updateEvaluates(int year, Semester semester);
    // 사전에 파일 형태로 준비된 데이터를 읽어서 DB에 저장하는 메서드.
    public abstract void updateDepartments();
    public abstract void updateDepartmentShorten();
}
