package SmartAirAndHealthMointoring.demo.Repository;

import SmartAirAndHealthMointoring.demo.constants.Roles;
import SmartAirAndHealthMointoring.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User,String> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAllByRole(Roles roles);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Roles role);
}
