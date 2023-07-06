package NotableDeveloper.rank.service;

import NotableDeveloper.rank.domain.dto.DepartmentDto;
import NotableDeveloper.rank.domain.entity.Department;
import NotableDeveloper.rank.repository.DepartmentRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DepartmentService {
    DepartmentRepository departmentRepository;

    public List<DepartmentDto> getDepartments(){
        return departmentRepository.findAll().stream()
                .map(department -> DepartmentDto.builder()
                        .college(department.getCollege())
                        .departmentId(department.getId())
                        .originalName(department.getOriginalName())
                        .shortenedName(department.getShortenedName())
                        .build())
                .collect(Collectors.toList());
    }
}
