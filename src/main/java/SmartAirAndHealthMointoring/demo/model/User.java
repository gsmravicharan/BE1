package SmartAirAndHealthMointoring.demo.model;


import SmartAirAndHealthMointoring.demo.constants.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private String username;
    @Id
    private String Redgno;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;
}
