package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.ProfessorDetailDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.exceptiion.ProfessorNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ProfessorService {
    ProfessorRepository professorRepository;
    CourseProfessorRepository courseProfessorRepository;

    public List<ProfessorDto> getProfessorsByName(String name){
        List<Professor> professorsByName = professorRepository.findAllByNameContains(name);

        if(professorsByName.size() <= 0) throw new ProfessorNotFoundException();

        return professorsByName.stream()
                .map(p -> ProfessorDto.builder()
                        .professorId(p.getId())
                        .position(p.getPosition())
                        .department(p.getDepartment().getOriginalName())
                        .name(p.getName())
                        .tier(p.getTier())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ProfessorDto> getProfessorsByDepartment(Long departmentId){
        List<Professor> professorsByDepartment = professorRepository.findAllByDepartment_Id(departmentId);

        if(professorsByDepartment.size() <= 0) throw new ProfessorNotFoundException();

        return professorsByDepartment.stream()
                .map(p -> ProfessorDto.builder()
                        .name(p.getName())
                        .professorId(p.getId())
                        .tier(p.getTier())
                        .position(p.getPosition())
                        .department(p.getDepartment().getOriginalName())
                        .build())
                .collect(Collectors.toList());
    }
}
