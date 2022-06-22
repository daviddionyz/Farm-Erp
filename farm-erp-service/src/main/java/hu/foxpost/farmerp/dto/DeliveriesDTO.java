package hu.foxpost.farmerp.dto;

import hu.foxpost.farmerp.db.entity.*;
import lombok.Data;

@Data
public class DeliveriesDTO {

    private Integer id;
    private Integer diaryId;
    private Integer gross;
    private Integer empty;
    private Integer net;
    private String intakeDate;
    private WorkerEntity worker;
    private VehiclesEntity vehicle;
    private FieldEntity from;
    private StorageEntity where;
    private Boolean isCorpMoving;
    private StorageEntity fromStorage;

    private CropsEntity crop;

}
