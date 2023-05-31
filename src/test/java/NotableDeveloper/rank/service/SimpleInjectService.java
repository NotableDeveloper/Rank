package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.enums.Semester;
import NotableDeveloper.rank.domain.exceptiion.EvaluationAlreadyException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.CourseRepository;
import NotableDeveloper.rank.repository.DepartmentRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleInjectService implements DataInjectService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    CourseProfessorRepository courseProfessorRepository;

    @Autowired
    RankVersionRepository rankVersionRepository;

    @Override
    public void updateEvaluates(int year, Semester semester) {
        if(rankVersionRepository.existsYearAndSemester(year, semester))
            throw new EvaluationAlreadyException();

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
