package hu.foxpost.farmerp.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Data
@Service
public class Config {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${accestoken.expiration.time}")
    private Long exprTime;

}
