package SmartAirAndHealthMointoring.demo.serviceImpl;

import SmartAirAndHealthMointoring.demo.Repository.UserRepo;
import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AuthService;
import SmartAirAndHealthMointoring.demo.service.EmailService;
import SmartAirAndHealthMointoring.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    EmailService emailService;

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
    public ResponseEntity<ResponseDto<?>> sendOtp(String email) {
        // 1. Basic Validation
        if (!StringUtils.hasText(email)) {
            return new ResponseEntity<>(ResponseDto.error("Email is required"), HttpStatus.BAD_REQUEST);
        }

        try {
            // 2. Generate a 6-digit OTP
            // (int)(Math.random() * 900000) + 100000 ensures it's always 6 digits
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

            // 3. Prepare Email Content
            String subject = "Verification Code - Smart Air Monitoring";
            String message = "Hello,\n\nYour One-Time Password (OTP) for registration is: " + otp +
                    "\n\nThis code will expire in 5 minutes. Do not share this with anyone.";

            // 4. Send the Email
            emailService.sendSimpleEmail(email, subject, message);

            /**
             * NOTE: Right now, the OTP is "lost" after sending because we haven't
             * integrated Redis yet. Once we do, we will add:
             * redisTemplate.opsForValue().set(email, otp, Duration.ofMinutes(5));
             */

            return ResponseEntity.ok(
                    ResponseDto.success(null, "OTP sent successfully to " + email)
            );

        } catch (Exception e) {
            // This catches SMTP errors (wrong password, connection issues, etc.)
            return new ResponseEntity<>(
                    ResponseDto.error("Failed to send OTP: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.get("username"),user.get("password"))
        );

        if(authentication.isAuthenticated())
        {

            String Token = jwtService.generateToken(user.get("username"));

            return ResponseEntity.ok(
                    ResponseDto.success(Token,"Login Successful..!")
            );
        }
        else{
            return ResponseEntity.badRequest().body(
                    ResponseDto.error("Bad Request Wrong Credentials")
            );
        }


    }
}