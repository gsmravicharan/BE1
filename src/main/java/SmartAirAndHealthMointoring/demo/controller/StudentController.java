package SmartAirAndHealthMointoring.demo.controller;


import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.StudentDashboard;
import SmartAirAndHealthMointoring.demo.service.StudentDashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    @Autowired
    StudentDashBoardService studentDashBoardService;

    @GetMapping("/details")
    public String details()
    {
        return "Student details";
    }

    @PostMapping("/dashboard/create")
    public ResponseEntity<ResponseDto<?>> dashBoardCreation(@RequestBody StudentDashboard details)
    {
        System.out.println("Triggered dashBoard Creation..!");
        return studentDashBoardService.dashBoardCreation(details);
    }

    @GetMapping("/dashboard/get/{redgno}")
    public ResponseEntity<ResponseDto<?>> dashBoardGet(@PathVariable String redgno)
    {
        return studentDashBoardService.dashBoardGet(redgno);
    }

}
