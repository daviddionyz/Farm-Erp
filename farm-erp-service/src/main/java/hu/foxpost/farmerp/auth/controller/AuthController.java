package hu.foxpost.farmerp.auth.controller;


import hu.foxpost.farmerp.auth.dtos.LoginRequest;
import hu.foxpost.farmerp.auth.dtos.LoginResponse;
import hu.foxpost.farmerp.auth.service.IUserService;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final IUserService userService;

    @PostMapping("/login")
    public BaseResponseDTO signin(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout();
    }
}
