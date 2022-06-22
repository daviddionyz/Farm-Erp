package hu.foxpost.farmerp.dto.response;

import lombok.Data;

@Data
public class BaseResponseDTO {

    private String message;

    private Integer code;

    private Object data;

    public BaseResponseDTO(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public BaseResponseDTO(Object data) {
        this.code = 200;
        this.data = data;
        this.message = "success";
    }
}
