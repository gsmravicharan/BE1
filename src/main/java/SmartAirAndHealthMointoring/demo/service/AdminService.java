package SmartAirAndHealthMointoring.demo.service;

import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<ResponseDto<?>> getAllUsers();

    ResponseEntity<ResponseDto<?>> getByDept(String dept);

    ResponseEntity<ResponseDto<?>> users();

}
