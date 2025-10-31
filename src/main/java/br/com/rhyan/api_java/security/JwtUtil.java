package br.com.rhyan.api_java.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.security.Key;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "umaChaveSecretaSuperSeguraDeExemplo123456";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; //24 hora

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String gerarToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairUsuario(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Verifica se o token é válido
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado: " + e.getMessage());
        } catch (JwtException e) {
            System.out.println("Token inválido: " + e.getMessage());
        }
        return false;
    }

    private boolean isTokenExpirado(String token) {
        Date exp = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return exp.before(new Date());
    }


    public Authentication getAuthentication(String username, HttpServletRequest request) {
        User principal = new User(username, "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }


}
