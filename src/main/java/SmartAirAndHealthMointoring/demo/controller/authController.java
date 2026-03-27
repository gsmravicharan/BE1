package SmartAirAndHealthMointoring.demo.controller;


import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AuthService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class authController {


    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<?>> register(@RequestBody User user)
    {
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<?>> login(@RequestBody Map<String,String> user)
    {
        return authService.login(user);
    }


}
