package io.swagger.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JwtProvider {
    private String secretToken;
    private int validityTime = 36000000; //10h in milliseconds

    @PostConstruct
    protected void Initiate()
    {
        secretToken = Base64.getEncoder().encodeToString(secretToken.getBytes());
    }

    public String GenerateToken(String username, List<User.UserRoleEnum> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTime);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretToken)//
                .compact();
    }
}
