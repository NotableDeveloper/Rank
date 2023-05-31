package NotableDeveloper.rank.service;

public interface DataInjectService {
    // 크롤링, 파일 등으로 강의평가 데이터를 읽어서 DB에 저장하는 메서드.
    void updateEvaluates();
    // 사전에 파일 형태로 준비된 데이터를 읽어서 DB에 저장하는 메서드.
    void updateDepartments();
    void updateDepartmentShorten();
}
