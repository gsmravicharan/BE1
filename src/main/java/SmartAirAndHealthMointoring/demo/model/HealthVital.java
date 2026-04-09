package SmartAirAndHealthMointoring.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
public class HealthVital implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer air;

    @Column(length = 50)
    private String status;

    private Integer bpm;

    private Float spo2;

    private Float temp;

    @Column(length = 100)
    private String email;

    @Column(name = "registerno", length = 50)
    private String registerno;
    @Column(length = 100)
    private String department;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}