package SmartAirAndHealthMointoring.demo.Repository;

import SmartAirAndHealthMointoring.demo.model.HealthVital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthVitalRepo extends JpaRepository<HealthVital,Integer> {
    List<HealthVital> findByRegisterno(String redgno);

    List<HealthVital> findByDepartment(String dept);
}
