package io.swagger.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.model.User;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JwtProvider {
    //Nicky
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretToken;

    @Value("${security.jwt.token.expire-length:36000000}")
    private int validityTime = 36000000; //10h in milliseconds

    @Autowired
    private UserService thisUserService;


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

    public void GetAuthentication(){

    }

    public String GetUserName(String jwtToken){
        UserService userService = thisUserService.loadUser(GetUserName(jwtToken))
    }

    public void ResolveToken(){

    }
    public boolean ValidateToken(String jwtToken){
        try {
            Jwts.parser().setSigningKey(secretToken).parseClaimsJws(jwtToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT token is invalid or expired");
        }
    }
}
