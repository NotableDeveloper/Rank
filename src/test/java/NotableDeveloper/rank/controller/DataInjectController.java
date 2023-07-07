package NotableDeveloper.rank.controller;

import NotableDeveloper.rank.domain.request.EvaluateRequest;
import NotableDeveloper.rank.service.SimpleInjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataInjectController {
    private SimpleInjectService injectService;

    @PutMapping("/evaluates")
    void updateEvaluates(@RequestBody EvaluateRequest request){
        String response = "Year : " + request.getYear() + "Semester" + request.getSemester();
    }
}
