package SmartAirAndHealthMointoring.demo.serviceImpl;

import SmartAirAndHealthMointoring.demo.Repository.HealthVitalRepo;
import SmartAirAndHealthMointoring.demo.Repository.UserRepo;
import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.constants.Roles;
import SmartAirAndHealthMointoring.demo.model.HealthVital;
import SmartAirAndHealthMointoring.demo.model.User;
import SmartAirAndHealthMointoring.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public  class AdminServiceImpl implements AdminService {

    private final HealthVitalRepo healthVitalRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public ResponseEntity<ResponseDto<?>> getAllUsers() {
        try {
            List<HealthVital> vitals = healthVitalRepo.findAll();

            if (vitals.isEmpty()) {
                log.info("No health vital records found in database");
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(ResponseDto.error("No health records found"));
            }

            log.info("Successfully retrieved {} health vital records", vitals.size());
            return ResponseEntity.ok(
                    ResponseDto.success(vitals, "Health records retrieved successfully")
            );

        } catch (Exception e) {
            log.error("Critical error while fetching health vitals: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.error("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDto<?>> getByDept(String dept) {
        try {

            if (dept == null || dept.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ResponseDto.error("Department name cannot be empty"));
            }
            List<HealthVital> vitals = healthVitalRepo.findByDepartment(dept);

            if (vitals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseDto.error("No users found in department: " + dept));
            }

            return ResponseEntity.ok(
                    ResponseDto.success(vitals, "Users fetched for department: " + dept)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.error("Server error: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ResponseDto<?>> users() {
        try {
            List<User> users = userRepo.findAll();
            return ResponseEntity.ok(
                    ResponseDto.success(users, "All users fetched successfully")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDto.error("Server error: " + e.getMessage()));
        }
    }




}