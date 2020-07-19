package com.bachelor.thesisbe.security;

import com.bachelor.thesisbe.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils implements Serializable {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.duration}")
    private int duration;

    String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean checkTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserEntity userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", userDetails.getFirstName());
        claims.put("lastName", userDetails.getLastName());
        claims.put("image", userDetails.getImage());
        claims.put("colorCode", userDetails.getColorCode());
        claims.put("userId", userDetails.getId());
        return doGenerateToken(claims, userDetails.getEmail());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validate token
    Boolean validateToken(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !checkTokenExpired(token));
    }

}
