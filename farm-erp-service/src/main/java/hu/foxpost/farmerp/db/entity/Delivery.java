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
public class Delivery implements Comparable<Delivery>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer diaryId;

    private Integer gross;

    private Integer empty;

    private Integer net;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Worker worker;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Vehicle vehicle;

    private Date intakeDate;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Field from;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Storage fromStorage;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    private Storage where;

    private Boolean isCorpMoving;

    private String cropName;
    private String cropType;

    @Override
    public int compareTo(Delivery o) {
        if (this.intakeDate.equals(o.intakeDate)){
            return 0;
        } else if(this.intakeDate.getTime() < o.intakeDate.getTime()){
            return 1;
        }else{
            return -1;
        }
    }
}
