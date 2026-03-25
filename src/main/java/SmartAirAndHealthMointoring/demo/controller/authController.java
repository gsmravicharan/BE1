package SmartAirAndHealthMointoring.demo.controller;


import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
