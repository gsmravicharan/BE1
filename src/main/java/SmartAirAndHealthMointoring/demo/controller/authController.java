package SmartAirAndHealthMointoring.demo.controller;


import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
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

    @PostMapping("/send-otp")
    public ResponseEntity<ResponseDto<?>> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (!StringUtils.hasText(email)) {
            return ResponseEntity.badRequest().body(ResponseDto.error("Email is required"));
        }

        return authService.sendOtp(email);
    }

}
