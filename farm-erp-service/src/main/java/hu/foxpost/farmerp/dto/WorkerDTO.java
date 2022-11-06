package hu.foxpost.farmerp.dto;

import lombok.Data;

import java.util.Date;

@Data
public class WorkerDTO {

    private Integer id;

    private String name;

    private Date joinDate;

    private Integer vehicle;

    private String position;
    private Boolean isDeleted;
}
