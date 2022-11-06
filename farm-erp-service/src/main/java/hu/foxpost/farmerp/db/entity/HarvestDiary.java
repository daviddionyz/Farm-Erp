package hu.foxpost.farmerp.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "farm_erp", name = "harvest_diaries")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HarvestDiary implements Comparable<HarvestDiary> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer year;

    @Override
    public int compareTo(HarvestDiary harvestDiaryEntity) {
        if(this.year.equals(harvestDiaryEntity.year))
            return 0;
        else if(this.year< harvestDiaryEntity.year)
            return 1;
        else
            return -1;
    }
}
