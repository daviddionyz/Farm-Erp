package hu.foxpost.farmerp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Builder
@Data
public class HarvestDiaryDTO {

    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("year")
    private Integer year;

}
