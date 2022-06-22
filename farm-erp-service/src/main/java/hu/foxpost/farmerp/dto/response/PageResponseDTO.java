package hu.foxpost.farmerp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponseDTO {

    private Integer allNumber;

    private List<Object> objects;

    private Integer page;

    private Integer pageSize;
}