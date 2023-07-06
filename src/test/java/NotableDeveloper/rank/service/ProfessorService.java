package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.CourseHistoryDto;
import NotableDeveloper.rank.domain.dto.ProfessorDetailDto;
import NotableDeveloper.rank.domain.dto.ProfessorDto;
import NotableDeveloper.rank.domain.entity.Course;
import NotableDeveloper.rank.domain.entity.CourseProfessor;
import NotableDeveloper.rank.domain.entity.Professor;
import NotableDeveloper.rank.domain.enums.Tier;
import NotableDeveloper.rank.domain.exceptiion.ProfessorNotFoundException;
import NotableDeveloper.rank.repository.CourseProfessorRepository;
import NotableDeveloper.rank.repository.ProfessorRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
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

    public ProfessorDetailDto getProfessorById(Long professorId){
        ArrayList<CourseProfessor> professorById = courseProfessorRepository.findAllByProfessor_Id(professorId);

        if(professorById.size() <= 0)
            throw new ProfessorNotFoundException();

        Professor p = professorById.get(0).getProfessor();

        List<CourseHistoryDto> offeredCourses = professorById.stream()
                .map(cp -> {
                    Course c = cp.getCourse();

                    return CourseHistoryDto.builder()
                            .courseId(c.getId())
                            .year(c.getOfferedYear())
                            .semester(c.getSemester())
                            .tier(c.getTier())
                            .title(c.getTitle())
                            .build();
                })
                .collect(Collectors.toList());

        return ProfessorDetailDto.builder()
                .name(p.getName())
                .college(p.getCollege())
                .department(p.getDepartment().getOriginalName())
                .professorId(p.getId())
                .professorTier(p.getTier())
                .courseCount(offeredCourses.size())
                .position(p.getPosition())
                .offeredCourses(offeredCourses)
                .build();

    }

    public ArrayList<ProfessorDto> getHonorProfessors(){
        ArrayList<ProfessorDto> honors = new ArrayList<>();
        professorRepository.findAllByTierNot(Tier.U,
                Sort.by(Sort.Direction.DESC, "average"))
                .forEach(professor -> {
                    if(honors.size() < 10 && professor.getCount() >= 15)
                        honors.add(ProfessorDto.builder()
                                .tier(professor.getTier())
                                .name(professor.getName())
                                .department(professor.getDepartment().getOriginalName())
                                .position(professor.getPosition())
                                .professorId(professor.getId())
                                .build());
                        }
                );

        return honors;
    }
}
