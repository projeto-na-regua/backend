package projetopi.projetopi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import projetopi.projetopi.dominio.Usuario;
@Component
public class Token {

    String token;
    private static final String tokenSecretKey = "R2x5q8tBwEzHcTfWjYn3r6u9x$B&E)H@McQfTjWnZr4t7w!z%C*F-JaNdRgUkXp";

    public String getToken(Usuario usuario) {
        long exp = System.currentTimeMillis() + (15 * 60 * 1000);

        token = JWT.create()
                .withClaim("id", usuario.getId().toString())
                .withClaim("nome", usuario.getNome())
                .withClaim("senha", usuario.getSenha())
                .withClaim("email", usuario.getEmail())
                .withClaim("exp", exp)
                .sign(Algorithm.HMAC256(tokenSecretKey));

        return token;
    }

    public Integer getUserIdByToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getClaims().get("id").asInt();
    }

    public String getNomeByToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getClaims().get("nome").asString();
    }

    public void addTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setMaxAge(15);
        cookie.setPath("/");
        response.addCookie(cookie);
    }



}
