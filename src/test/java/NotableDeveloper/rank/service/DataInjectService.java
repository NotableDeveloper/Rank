package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.EvaluationDto;
import NotableDeveloper.rank.domain.dto.ShortenDepartmentDto;
import NotableDeveloper.rank.domain.enums.Semester;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataInjectService  {
    // 크롤링, 파일 등으로 강의평가 데이터를 읽어서 DB에 저장하는 메서드.
    void updateEvaluates(int year, Semester semester, List<EvaluationDto> evaluations);
    // 사전에 파일 형태로 준비된 데이터를 읽어서 DB에 저장하는 메서드.
    void updateDepartmentShorten(List<ShortenDepartmentDto> shortenDepartments);
}
