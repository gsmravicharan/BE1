package SmartAirAndHealthMointoring.demo.service;

import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {

    ResponseEntity<ResponseDto<?>> register(User user);

    ResponseEntity<ResponseDto<?>> login(Map<String, String> user);

    ResponseEntity<ResponseDto<?>> sendOtp(String email);
}
