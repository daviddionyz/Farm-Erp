package hu.foxpost.farmerp.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "farm_erp", name = "deliveries")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveriesEntity implements Comparable<DeliveriesEntity>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer diaryId;

    private Integer gross;

    private Integer empty;

    private Integer net;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private WorkerEntity worker;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private VehiclesEntity vehicle;

    private Date intakeDate;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private FieldEntity from;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private StorageEntity fromStorage;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private StorageEntity where;

    private Boolean isCorpMoving;

    @Override
    public int compareTo(DeliveriesEntity o) {
        if (this.intakeDate.equals(o.intakeDate)){
            return 0;
        } else if(this.intakeDate.getTime() < o.intakeDate.getTime()){
            return 1;
        }else{
            return -1;
        }
    }
}
