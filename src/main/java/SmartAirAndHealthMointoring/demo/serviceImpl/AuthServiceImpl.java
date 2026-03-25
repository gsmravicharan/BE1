package SmartAirAndHealthMointoring.demo.serviceImpl;

import SmartAirAndHealthMointoring.demo.Repository.UserRepo;
import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<ResponseDto<?>> register(User user) {

        if (user == null) {
            return new ResponseEntity<>(ResponseDto.error("Request body cannot be null"), HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(user.getRedgno()) ||
                !StringUtils.hasText(user.getUsername()) ||
                !StringUtils.hasText(user.getEmail()) ||
                !StringUtils.hasText(user.getPassword()) ||
                user.getRole() == null) {

            return new ResponseEntity<>(
                    ResponseDto.error("Missing required fields: Registration No, Username, Email, Password, or Role"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (userRepo.existsById(user.getRedgno())) {
            return new ResponseEntity<>(
                    ResponseDto.error("User already exists with Registration No: " + user.getRedgno()),
                    HttpStatus.CONFLICT
            );
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepo.save(user);

            savedUser.setPassword(null);

            return new ResponseEntity<>(
                    ResponseDto.success(savedUser, "User registered successfully"),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ResponseDto.error("Internal Server Error: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}