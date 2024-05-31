package com.studymate.service;

import com.studymate.model.User;
import com.studymate.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


@Service
public class AuthenticationService {

    private static final Logger log = LogManager.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String secretKey = "044561dc31ea927d424cf34c400e6c2433e6488aaa797f37a8a2a33c50303d91";
    private final long expiration = 86400000;


    @Autowired
    public AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> loginUser(String username, String password) {
        log.info("Logging in user");
        User user = userRepository.findByUserName(username);
        if(user == null) {
            String errorMsg = "User not found";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }
        if(passwordEncoder.matches(password, user.getEncryptedPassword())) {
            String token = generateToken(username);
            log.info("User logged in successfully");
            String tokenWithBearer = "Bearer " + token;
            validateToken(tokenWithBearer);
            return ResponseEntity.ok(token);
        }
        else {
            String errorMsg = "Incorrect password";
            log.error(errorMsg);
            return ResponseEntity.badRequest().body(errorMsg);
        }
    }

    private String generateToken(String username) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + expiration);

        SecretKey signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(exp)
                .signWith(signingKey)
                .compact();
    }

    public boolean validateToken(String token) {
        if(token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        String actualToken = token.substring(7);
        try {
            SecretKey signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(actualToken);
            return true;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        SecretKey signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
}
