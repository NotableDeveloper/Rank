package NotableDeveloper.rank.service;

import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorService {
    ProfessorRepository professorRepository;
    CourseProfessorRepository courseProfessorRepository;
}
