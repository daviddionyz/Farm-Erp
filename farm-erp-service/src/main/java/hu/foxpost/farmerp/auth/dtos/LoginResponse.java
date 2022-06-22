package hu.foxpost.farmerp.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private final String token;
    private String name;
    private Integer role;
    private String email;
}
