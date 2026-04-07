package SmartAirAndHealthMointoring.demo.controller;

import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.service.HealthVitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vitals/")
@CrossOrigin(origins = "http://localhost:5173")
public class HealthVitalController {

    @Autowired
    HealthVitalService healthVitalService;

    @GetMapping("/vitals")
    public ResponseEntity<ResponseDto<?>> vitals()
    {
        return healthVitalService.vitals();
    }

    @GetMapping("/vital/{redgno}")
    public ResponseEntity<ResponseDto<?>> vital(@PathVariable String redgno)
    {
        return healthVitalService.vital(redgno);
    }
}
