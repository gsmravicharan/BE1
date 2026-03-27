package SmartAirAndHealthMointoring.demo.controller;

import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.HodDashboard;
import SmartAirAndHealthMointoring.demo.service.HodDashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hod")
@CrossOrigin(origins = "http://localhost:5173")
public class HodController {

    @Autowired
    HodDashBoardService hodDashBoardService;

    @GetMapping("/details")
    public String details()
    {
        return "hod Details";
    }

    @PostMapping("/dashboard/create")
    public ResponseEntity<ResponseDto<?>> dashBoardCreate(@RequestBody HodDashboard details)
    {
        return hodDashBoardService.dashBoardCreate(details);
    }

    @GetMapping("/dashboard/get/{redgno}")
    public ResponseEntity<ResponseDto<?>> dashBoardGet(@PathVariable String redgno)
    {
        return hodDashBoardService.dashBoardGet(redgno);
    }

    @GetMapping("/all/students")
    public ResponseEntity<ResponseDto<?>> allStudents()
    {
        return hodDashBoardService.allStudents();
    }

    @GetMapping("/all/faculty")
    public ResponseEntity<ResponseDto<?>> allFaculty()
    {
        return hodDashBoardService.allFaculty();
    }

}
