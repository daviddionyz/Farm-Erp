package hu.foxpost.farmerp.auth.service;

import hu.foxpost.farmerp.auth.dtos.LoginRequest;
import hu.foxpost.farmerp.auth.dtos.LoginResponse;
import hu.foxpost.farmerp.auth.utils.JwtUtils;
import hu.foxpost.farmerp.auth.utils.UserDetailsImpl;
import hu.foxpost.farmerp.db.entity.User;
import hu.foxpost.farmerp.db.repository.UserRepository;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Component
public class UserService implements IUserService{

    private final JwtUtils tokenUtil;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public UserService(JwtUtils tokenUtil,
                       UserRepository userRepository,
                       AuthenticationManager authenticationManager) {
        this.tokenUtil = tokenUtil;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public String getCourierName(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getName).orElse(null);
    }

    @Override
    public BaseResponseDTO login(LoginRequest loginRequest) {
        LoginResponse loginResponse;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            loginResponse = new LoginResponse(tokenUtil.generateJwtToken(authentication), userDetails.getUsername(), userDetails.getRole(), userDetails.getEmail());

            return new BaseResponseDTO(loginResponse);

        } catch (Exception e ){
            return new BaseResponseDTO("Authentication failed",500);
        }
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void logout(){
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
