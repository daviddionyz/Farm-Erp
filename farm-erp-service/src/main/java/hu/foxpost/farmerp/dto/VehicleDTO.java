package hu.foxpost.farmerp.dto;

import lombok.Data;

@Data
public class VehicleDTO {

    private Integer id;

    private String name;

    private String type;

    private Integer status;
    private Boolean isDeleted;
}
