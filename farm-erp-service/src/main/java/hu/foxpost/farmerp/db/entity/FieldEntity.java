package hu.foxpost.farmerp.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "farm_erp", name = "field")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer size;

    private String corpName;
    private String corpType;
}
