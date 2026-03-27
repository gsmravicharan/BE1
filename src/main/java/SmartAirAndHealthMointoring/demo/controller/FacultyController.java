package SmartAirAndHealthMointoring.demo.controller;

import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.TeacherDashBoard;
import SmartAirAndHealthMointoring.demo.service.TeacherDashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "http://localhost:5173")
public class FacultyController {

    @Autowired
    TeacherDashBoardService teacherDashBoardService;

    @GetMapping("/details")
    public String details()
    {
        return "Faculty Details";
    }

    @PostMapping("/dashboard/create")
    public ResponseEntity<ResponseDto<?>> dashBoardCreation(@RequestBody TeacherDashBoard details)
    {
        return teacherDashBoardService.dashBoardcreation(details);
    }

    @GetMapping("/dashboard/get/{redgno}")
    public ResponseEntity<ResponseDto<?>> dashBoardGet(@PathVariable String redgno)
    {
        return  teacherDashBoardService.dashBoardGet(redgno);
    }
}
