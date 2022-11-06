package hu.foxpost.farmerp.auth.service;

import hu.foxpost.farmerp.auth.dtos.LoginRequest;
import hu.foxpost.farmerp.auth.dtos.LoginResponse;
import hu.foxpost.farmerp.db.entity.User;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

public interface IUserService {

    BaseResponseDTO login(LoginRequest loginRequest);

    void deleteById(Long userId);

    User getUserById(Long id);

    void logout();
}
