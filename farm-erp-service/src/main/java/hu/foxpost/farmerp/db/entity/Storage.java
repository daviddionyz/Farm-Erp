package hu.foxpost.farmerp.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "farm_erp", name = "storages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer capacity;

    private Integer fullness;
    private Boolean isDeleted;
}
