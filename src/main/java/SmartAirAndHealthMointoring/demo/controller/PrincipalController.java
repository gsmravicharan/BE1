package SmartAirAndHealthMointoring.demo.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/principal")
@CrossOrigin(origins = "http://localhost:5173")
public class PrincipalController {

    @GetMapping("/details")
    public String details()
    {
        return "Principal Details";
    }
}
