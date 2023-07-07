package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.domain.request.DepartmentShortenRequest;
import NotableDeveloper.rank.domain.request.EvaluateRequest;
import NotableDeveloper.rank.service.SimpleInjectService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
@Getter
@Setter
public class DataInjectController {
    @Autowired
    SimpleInjectService injectService;

    @PostMapping("/evaluates")
    void saveEvaluates(@RequestBody EvaluateRequest request){
        injectService.saveEvaluates(request.getYear(), request.getSemester());
    }

    @PutMapping("/department")
    void updateShortenDepartment(@RequestBody DepartmentShortenRequest request){
        injectService.updateDepartments(request.getYear(), request.getSemester());
    }
}
