package SmartAirAndHealthMointoring.demo.serviceImpl;

import SmartAirAndHealthMointoring.demo.Repository.UserRepo;
import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AuthService;
import SmartAirAndHealthMointoring.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;


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

        if(userRepo.existsByUsername(user.getUsername()))
        {
            return new ResponseEntity<>(
                    ResponseDto.error("User already exists with this Username " + user.getUsername()),
                    HttpStatus.CONFLICT
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

    @Override
    public ResponseEntity<ResponseDto<?>> login(Map<String, String> user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.get("username"), user.get("password"))
            );

            if (authentication.isAuthenticated()) {
                User userEntity = userRepo.findByUsername(user.get("username"))
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String token = jwtService.generateToken(user.get("username"));

                // Data returned to the frontend
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", token);
                authData.put("role", userEntity.getRole().name());
                authData.put("redgno", userEntity.getRedgno());

                return ResponseEntity.ok(ResponseDto.success(authData, "Login Successful"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ResponseDto.error("Invalid Credentials"));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDto.error("Invalid Username or Password"));
        }
    }
}