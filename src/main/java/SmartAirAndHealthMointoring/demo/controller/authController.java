package SmartAirAndHealthMointoring.demo.controller;


import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class authController {

    @Autowired
    AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<ResponseDto<?>> sendOtp(@RequestBody Map<String, String> request) {
        return authService.sendOtp(request.get("email"));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<?>> register(@RequestBody User user, @RequestParam String otp) {
        return authService.register(user, otp);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<?>> login(@RequestBody Map<String, String> user, @RequestParam String otp) {
        return authService.login(user, otp);
    }
}
