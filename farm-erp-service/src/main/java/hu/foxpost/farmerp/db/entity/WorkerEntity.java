package hu.foxpost.farmerp.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "farm_erp", name = "workers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Date joinDate;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    @Nullable
    private VehiclesEntity vehicle;

    private String position;

    private Boolean isDeleted;
}
