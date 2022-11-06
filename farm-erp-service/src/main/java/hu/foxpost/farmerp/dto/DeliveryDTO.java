package hu.foxpost.farmerp.dto;

import hu.foxpost.farmerp.db.entity.*;
import lombok.Data;

@Data
public class DeliveryDTO {

    private Integer id;
    private Integer diaryId;
    private Integer gross;
    private Integer empty;
    private Integer net;
    private String intakeDate;
    private Worker worker;
    private Vehicle vehicle;
    private Field from;
    private Storage where;
    private Boolean isCorpMoving;
    private Storage fromStorage;

    private Crop crop;

}
