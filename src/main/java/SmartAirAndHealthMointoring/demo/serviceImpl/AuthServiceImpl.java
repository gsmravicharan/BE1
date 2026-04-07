package SmartAirAndHealthMointoring.demo.serviceImpl;

import SmartAirAndHealthMointoring.demo.Repository.UserRepo;
import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AuthService;
import SmartAirAndHealthMointoring.demo.service.EmailService;
import SmartAirAndHealthMointoring.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<ResponseDto<?>> sendOtp(String email, boolean isLogin) {
        if (!StringUtils.hasText(email)) {
            return new ResponseEntity<>(ResponseDto.error("Email is required"), HttpStatus.BAD_REQUEST);
        }

        if (isLogin) {
            Optional<User> user = userRepo.findByEmail(email);
            if (user.isEmpty()) {
                return new ResponseEntity<>(ResponseDto.error("Email Not Registered..!"), HttpStatus.BAD_REQUEST);
            }
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        redisTemplate.opsForValue().set("OTP_" + email, otp, Duration.ofSeconds(300));

        String subject = "Verification Code - Smart Air Monitoring";
        String message = "Your OTP is: " + otp + ". Valid for 5 minutes.";

        try {
            emailService.sendSimpleEmail(email, subject, message);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseDto.error("Failed to send email"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(ResponseDto.success(null, "OTP sent to " + email));
    }

    @Override
    public ResponseEntity<ResponseDto<?>> register(User user, String otp) {
        String cachedOtp = redisTemplate.opsForValue().get("OTP_" + user.getEmail());

        if (cachedOtp == null || !cachedOtp.equals(otp)) {
            return new ResponseEntity<>(ResponseDto.error("Invalid or expired OTP"), HttpStatus.BAD_REQUEST);
        }

        if(userRepo.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>(ResponseDto.error("Username taken"), HttpStatus.CONFLICT);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        redisTemplate.delete("OTP_" + user.getEmail());

        return new ResponseEntity<>(ResponseDto.success(null, "Registered successfully"), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseDto<?>> login(Map<String, String> user, String otp) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.get("username"), user.get("password"))
            );

            if (authentication.isAuthenticated()) {
                User userEntity = userRepo.findByUsername(user.get("username"))
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String cachedOtp = redisTemplate.opsForValue().get("OTP_" + userEntity.getEmail());
                if (cachedOtp == null || !cachedOtp.equals(otp)) {
                    return new ResponseEntity<>(ResponseDto.error("Invalid or expired OTP"), HttpStatus.BAD_REQUEST);
                }

                String token = jwtService.generateToken(user.get("username"));
                redisTemplate.delete("OTP_" + userEntity.getEmail());

                Map<String, Object> authData = new HashMap<>();
                authData.put("token", token);
                authData.put("role", userEntity.getRole().name());
                authData.put("redgno", userEntity.getRedgno());

                return ResponseEntity.ok(ResponseDto.success(authData, "Login Successful"));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.error("Unauthorized"));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.error("Invalid Credentials"));
        }
    }
}