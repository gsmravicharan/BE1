package SmartAirAndHealthMointoring.demo.service;

import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<ResponseDto<?>> register(User user);
}
