package SmartAirAndHealthMointoring.demo.controller;

import SmartAirAndHealthMointoring.demo.configuration.ResponseDto;
import SmartAirAndHealthMointoring.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

  @GetMapping("/allusers/vitals")
  public ResponseEntity<ResponseDto<?>> getAllUsers()
  {
      return adminService.getAllUsers();
  }

  @GetMapping("/allusers/{dept}")
  public ResponseEntity<ResponseDto<?>> getByDept(@PathVariable String dept)
  {
      return adminService.getByDept(dept);

  }

  @GetMapping("/users")
  public ResponseEntity<ResponseDto<?>> users()
  {
      return adminService.users();
  }

}
