package com.example.bloodbankmanagement.common.untils;

import com.example.bloodbankmanagement.service.authorization.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    public static final String EXCEPTION = "exception";
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static final long REFRESH_EXPIRE_TIME =  (60 * 60 * 1000L) * 8; // 8hour
    @Value("${jwtSecret}")
    private String jwtSecret;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + REFRESH_EXPIRE_TIME);
        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJWTToken(HttpServletRequest request, String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            request.setAttribute(EXCEPTION, "MalformedJwtException");
        } catch (ExpiredJwtException e) {
            request.setAttribute(EXCEPTION, "ExpiredJwtException");
        } catch (UnsupportedJwtException e) {
            request.setAttribute(EXCEPTION, "UnsupportedJwtException");
        } catch (IllegalArgumentException e) {
            request.setAttribute(EXCEPTION, "IllegalArgumentException");
        }
        return false;
    }
}
