package hu.foxpost.farmerp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageDTO {

    private Integer id;

    private String name;

    private Integer capacity;

    private Integer fullness;
}
