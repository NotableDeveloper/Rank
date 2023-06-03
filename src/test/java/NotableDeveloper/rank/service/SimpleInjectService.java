package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.entity.RankVersion;
import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.repository.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@AllArgsConstructor
public class SimpleInjectService extends DataInjectService {
    CourseRepository courseRepository;

    ProfessorRepository professorRepository;

    DepartmentRepository departmentRepository;

    CourseProfessorRepository courseProfessorRepository;

    RankVersionRepository rankVersionRepository;


    public void updateEvaluates(int year, Semester semester) {
        if(rankVersionRepository.existsByYearAndSemester(year, semester))
            throw new EvaluationAlreadyException();

        rankVersionRepository.save(new RankVersion(year, semester));

        /*
            To do : 데이터 주입 처리
         */
    }

    @Override
    public void updateDepartments() {

    }

    @Override
    public void updateDepartmentShorten() {

    }
}
