package hu.foxpost.farmerp.auth.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.foxpost.farmerp.db.entity.UserEntity;
import hu.foxpost.farmerp.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String email;
    private Integer role;
    private Boolean enable;

    @JsonIgnore
    private String password;

    private List<GrantedAuthority> authorities;

    public static UserDetailsImpl build(UserEntity user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                true,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(setRole(user))));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    private static String setRole(UserEntity user) {
        return Roles.getRole(user.getRole());
    }
}
